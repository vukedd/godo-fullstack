package com.app.godo.services.auth;

import com.app.godo.dtos.accountRequest.AccountRequestDto;
import com.app.godo.dtos.accountRequest.AccountRequestSuccessDto;
import com.app.godo.dtos.auth.AuthenticationRequestDto;
import com.app.godo.dtos.auth.AuthenticationResponseDto;
import com.app.godo.exceptions.general.NotFoundException;
import com.app.godo.exceptions.general.UnauthorizedException;
import com.app.godo.exceptions.refreshToken.ValidationException;
import com.app.godo.exceptions.registration.RegistrationException;
import com.app.godo.mappers.AccountRequestMapper;
import com.app.godo.models.AccountRequest;
import com.app.godo.models.RefreshToken;
import com.app.godo.models.User;
import com.app.godo.repositories.accountRequest.AccountRequestRepository;
import com.app.godo.repositories.auth.RefreshTokenRepository;
import com.app.godo.repositories.user.UserRepository;
import com.app.godo.services.email.EmailService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AccountRequestRepository accountRequestRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;

    private static final Logger logger = LogManager.getLogger(AuthService.class);


    @Value("${application.security.jwt.refresh-token-ttl}")
    private Long refreshTokenExpireTime;

    @Transactional
    public AccountRequestSuccessDto sendRegistrationRequest(AccountRequestDto accountRequestDto) {
        logger.info("New registration request received for username: {} and email: {}",
                accountRequestDto.getUsername(), accountRequestDto.getEmail());

        User userWithSameEmail = userRepository.findByEmail(accountRequestDto.getEmail());
        if (userWithSameEmail != null) {
            logger.warn("Registration failed for username: {}. Reason: Email {} is already taken.",
                    accountRequestDto.getUsername(), accountRequestDto.getEmail());

            throw new RegistrationException("The email you've entered is already taken!", RegistrationException.ErrorType.EMAIL_TAKEN);
        }

        Optional<User> userWithSameUsername = userRepository.findByUsername(accountRequestDto.getUsername());
        if (userWithSameUsername.isPresent()) {
            logger.warn("Registration failed for username: {}. Reason: Username is already taken.",
                    accountRequestDto.getUsername());
            throw new RegistrationException("The username you've entered is already taken!", RegistrationException.ErrorType.USERNAME_TAKEN);
        }

        logger.debug("Username and email are unique for {}. Proceeding with password hashing.", accountRequestDto.getUsername());
        String hashed = passwordEncoder.encode(accountRequestDto.getPassword());
        accountRequestDto.setPassword(hashed);

        AccountRequest registrationRequest = AccountRequestMapper.toAccountRequest(accountRequestDto);

        accountRequestRepository.save(registrationRequest);

        logger.info("Successfully saved registration request for username: {} with ID: {}",
                registrationRequest.getUsername(), registrationRequest.getId());
        emailService.sendRegistrationRequestSuccessEmail(registrationRequest.getUsername(), registrationRequest.getEmail());

        logger.info("Registration request process completed successfully for username: {}", registrationRequest.getUsername());
        return AccountRequestMapper.toAccountRequestSuccessDto(registrationRequest);
    }


    public AuthenticationResponseDto authenticate(
            final AuthenticationRequestDto request) {

        logger.info("Authentication attempt for user: {}", request.getUsername());

        try {
            final var authToken = UsernamePasswordAuthenticationToken
                    .unauthenticated(request.getUsername(), request.getPassword());

            final var authentication = authenticationManager.authenticate(authToken);

            logger.debug("Spring Security successfully authenticated user: {}", request.getUsername());

            var userEntity = userRepository.findByUsername(request.getUsername()).get();

            final var token = jwtService.generateToken(userEntity);

            logger.debug("JWT generated for user: {}", userEntity.getUsername());

            RefreshToken refreshToken = new RefreshToken();
            refreshToken.setUser(userEntity);
            refreshToken.setCreatedAt(Instant.now());
            refreshToken.setExpiresAt(Instant.now().plus(Duration.ofMillis(refreshTokenExpireTime)));
            refreshTokenRepository.save(refreshToken);

            logger.info("Created and saved refresh token with ID {} for user: {}", refreshToken.getId(), userEntity.getUsername());

            logger.info("User {} authenticated successfully.", userEntity.getUsername());

            return new AuthenticationResponseDto(token, refreshToken.getId());

        } catch (AuthenticationException e) {
            logger.warn("Authentication failed for user: {}. Reason: {}", request.getUsername(), e.getMessage());
            throw e;
        }
    }

    public AuthenticationResponseDto refreshToken(Long refreshTokenId) {
        logger.info("Access token refresh attempt received for refresh token ID: {}", refreshTokenId);

        final var refreshTokenEntity = refreshTokenRepository
                .findByIdAndExpiresAtAfter(refreshTokenId, Instant.now())
                .orElseThrow(() -> {
                    logger.warn("Token refresh failed. Invalid or expired refresh token ID: {}", refreshTokenId);
                    return new ValidationException("Invalid or expired refresh token!", ValidationException.ErrorType.BAD_REQUEST);
                });

        String username = refreshTokenEntity.getUser().getUsername();
        logger.debug("Found valid refresh token for user '{}'. Generating new access token.", username);

        final var newAccessToken = jwtService.generateToken(refreshTokenEntity.getUser());

        // INFO: The final successful outcome.
        logger.info("Successfully refreshed access token for user: {}", username);
        return new AuthenticationResponseDto(newAccessToken, refreshTokenId);
    }


    public void logout(long refreshTokenId) {
        logger.info("Logout request received for refresh token ID: {}", refreshTokenId);

        RefreshToken refreshToken = refreshTokenRepository.findById(refreshTokenId)
                .orElseThrow(() -> {
                    logger.warn("Logout failed. Refresh token with ID {} was not found.", refreshTokenId);
                    return new NotFoundException("Refresh token not found");
                });

        String username = refreshToken.getUser().getUsername();

        refreshTokenRepository.delete(refreshToken);

        logger.info("User '{}' successfully logged out. Refresh token ID {} deleted.", username, refreshTokenId);
    }
}
