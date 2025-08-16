package com.app.godo.mappers;

import com.app.godo.dtos.accountRequest.AccountRequestDto;
import com.app.godo.dtos.accountRequest.AccountRequestSuccessDto;
import com.app.godo.enums.RequestStatus;
import com.app.godo.models.AccountRequest;

import java.time.LocalDateTime;

public class AccountRequestMapper {
    public static AccountRequestSuccessDto toAccountRequestSuccessDto(AccountRequest accountRequest) {
        if (accountRequest == null) return null;

        return AccountRequestSuccessDto.builder()
                .email(accountRequest.getEmail())
                .username(accountRequest.getUsername())
                .status(accountRequest.getStatus())
                .submittedAt(accountRequest.getSubmittedAt())
                .build();
    }

    public static AccountRequest toAccountRequest(AccountRequestDto accountRequestDto) {
        if (accountRequestDto == null) return null;

        return AccountRequest.builder()
                .email(accountRequestDto.getEmail())
                .password(accountRequestDto.getPassword())
                .username(accountRequestDto.getUsername())
                .status(RequestStatus.PENDING)
                .submittedAt(LocalDateTime.now())
                .build();
    }
}
