package com.app.godo.services.accountRequest;


import com.app.godo.dtos.accountRequest.RejectRequestDto;
import com.app.godo.dtos.accountRequest.ApprovedRegistrationRequestDto;
import com.app.godo.dtos.accountRequest.PendingAccountRequestDto;
import com.app.godo.dtos.accountRequest.RejectRegistrationRequestDto;
import com.app.godo.enums.ProfileStatus;
import com.app.godo.enums.RequestStatus;
import com.app.godo.exceptions.general.NotFoundException;
import com.app.godo.models.AccountRequest;
import com.app.godo.models.User;
import com.app.godo.repositories.accountRequest.AccountRequestRepository;
import com.app.godo.repositories.user.UserRepository;
import com.app.godo.services.email.EmailService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountRequestService {
    private final AccountRequestRepository accountRequestRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    public List<PendingAccountRequestDto> getPendingAccountRequests() {
        List<AccountRequest> accountRequestList = accountRequestRepository.findAll();

        List<PendingAccountRequestDto> pendingRequests = accountRequestList.stream()
                .filter(request -> request.getStatus() == RequestStatus.PENDING)
                .map(request ->
                        new PendingAccountRequestDto(
                                request.getId(),
                                request.getUsername(),
                                request.getEmail(),
                                request.getSubmittedAt())).toList();

        return pendingRequests;
    }


    @Transactional
    public ApprovedRegistrationRequestDto approveRequest(long accountRequestId) {
        AccountRequest accountRequest = accountRequestRepository.findById(accountRequestId)
                .orElseThrow(() -> new NotFoundException("AccountRequest not found with id " + accountRequestId));

        accountRequest.setStatus(RequestStatus.ACCEPTED);
        accountRequestRepository.save(accountRequest);

        User newUser = User.builder()
                .username(accountRequest.getUsername())
                .password(accountRequest.getPassword())
                .email(accountRequest.getEmail())
                .profileStatus(ProfileStatus.PENDING)
                .memberSince(LocalDate.from(LocalDateTime.now())).build();

        userRepository.save(newUser);

        emailService.sendRegistrationRequestApprovedEmail(newUser.getUsername(), newUser.getEmail());

        return new ApprovedRegistrationRequestDto("Registration request successfully approved!");
    }

    public RejectRegistrationRequestDto rejectRequest(RejectRequestDto rejectRequestDto) {
        AccountRequest accountRequest = accountRequestRepository.findById(rejectRequestDto.getRequestId())
                .orElseThrow(() -> new NotFoundException("AccountRequest not found with id " + rejectRequestDto.getRequestId()));

        accountRequest.setStatus(RequestStatus.REJECTED);
        accountRequest.setRejectionReason(rejectRequestDto.getReason());
        accountRequestRepository.save(accountRequest);

        emailService.sendRegistrationRequestDeclineEmail(accountRequest.getUsername(), accountRequest.getEmail(), accountRequest.getRejectionReason());

        return new RejectRegistrationRequestDto("Registration request successfully rejected!");
    }

}
