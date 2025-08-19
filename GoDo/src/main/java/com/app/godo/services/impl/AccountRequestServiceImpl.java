package com.app.godo.services.impl;

import com.app.godo.dtos.accountRequest.AccountRequestDto;
import com.app.godo.dtos.accountRequest.AccountRequestSuccessDto;
import com.app.godo.exceptions.registration.RegistrationException;
import com.app.godo.mappers.AccountRequestMapper;
import com.app.godo.models.AccountRequest;
import com.app.godo.models.User;
import com.app.godo.repositories.accountRequest.AccountRequestRepository;
import com.app.godo.repositories.user.UserRepository;
import com.app.godo.services.AccountRequestService;
import com.app.godo.services.EmailService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountRequestServiceImpl implements AccountRequestService {
    private final AccountRequestRepository accountRequestRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Autowired
    public AccountRequestServiceImpl(AccountRequestRepository accountRequestRepository, UserRepository userRepository, EmailService emailService) {
        this.accountRequestRepository = accountRequestRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.emailService = emailService;
    }


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
}
