package com.app.godo.services;

import com.app.godo.dtos.accountRequest.AccountRequestDto;
import com.app.godo.dtos.accountRequest.AccountRequestSuccessDto;
import org.springframework.stereotype.Service;

@Service
public interface AccountRequestService {
    public AccountRequestSuccessDto sendRegistrationRequest(AccountRequestDto accountRequestDto);
}
