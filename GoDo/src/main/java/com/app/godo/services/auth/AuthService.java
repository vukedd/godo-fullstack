package com.app.godo.services.auth;

import com.app.godo.dtos.accountRequest.AccountRequestDto;
import com.app.godo.dtos.accountRequest.AccountRequestSuccessDto;
import com.app.godo.dtos.auth.AuthenticationRequestDto;
import com.app.godo.dtos.auth.AuthenticationResponseDto;
import com.app.godo.exceptions.registration.RegistrationException;
import com.app.godo.mappers.AccountRequestMapper;
import com.app.godo.models.AccountRequest;
import com.app.godo.models.User;
import com.app.godo.repositories.accountRequest.AccountRequestRepository;
import com.app.godo.repositories.user.UserRepository;
import com.app.godo.services.email.EmailService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AccountRequestRepository accountRequestRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Transactional
    public AccountRequestSuccessDto sendRegistrationRequest(AccountRequestDto accountRequestDto) {
        User userWithSameEmail = userRepository.findByEmail(accountRequestDto.getEmail());
        if (userWithSameEmail != null) {
            throw new RegistrationException("The email you've entered is already taken!", RegistrationException.ErrorType.EMAIL_TAKEN);
        }

        User userWithSameUsername = userRepository.findByUsername(accountRequestDto.getUsername());
        if (userWithSameUsername != null) {
            throw new RegistrationException("The username you've entered is already taken!", RegistrationException.ErrorType.USERNAME_TAKEN);
        }

        String hashed = passwordEncoder.encode(accountRequestDto.getPassword());
        accountRequestDto.setPassword(hashed);

        AccountRequest registrationRequest = AccountRequestMapper.toAccountRequest(accountRequestDto);

        accountRequestRepository.save(registrationRequest);

        emailService.sendRegistrationRequestSuccessEmail(registrationRequest.getUsername(), registrationRequest.getEmail());

        return AccountRequestMapper.toAccountRequestSuccessDto(registrationRequest);
    }


    public AuthenticationResponseDto authenticate(
            final AuthenticationRequestDto request) {

        final var authToken = UsernamePasswordAuthenticationToken
                .unauthenticated(request.getUsername(), request.getPassword());

        final var authentication = authenticationManager.authenticate(authToken);

        var userEntity = userRepository.findByUsername(request.getUsername());

        final var token = jwtService.generateToken(userEntity);

        return new AuthenticationResponseDto(token);
    }
}
