package com.app.godo.services.accountRequest;

import com.app.godo.dtos.accountRequest.AccountRequestDto;
import com.app.godo.dtos.accountRequest.AccountRequestSuccessDto;
import com.app.godo.exceptions.registration.RegistrationException;
import com.app.godo.mappers.AccountRequestMapper;
import com.app.godo.models.AccountRequest;
import com.app.godo.models.User;
import com.app.godo.repositories.accountRequest.AccountRequestRepository;
import com.app.godo.repositories.user.UserRepository;
import com.app.godo.services.email.EmailService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountRequestService {

}
