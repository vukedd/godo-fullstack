package com.app.godo.controllers.accountRequest;

import com.app.godo.dtos.accountRequest.AccountRequestDto;
import com.app.godo.dtos.accountRequest.AccountRequestSuccessDto;
import com.app.godo.models.AccountRequest;
import com.app.godo.services.AccountRequestService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accountRequest")
public class AccountRequestController {
    private final AccountRequestService accountRequestService;

    @Autowired
    public AccountRequestController(AccountRequestService accountRequestService) {
        this.accountRequestService = accountRequestService;
    }

    @PostMapping
    public ResponseEntity<AccountRequestSuccessDto> createAccountRequest(@Valid @RequestBody AccountRequestDto accountRequest){
        return ResponseEntity.ok(accountRequestService.sendRegistrationRequest(accountRequest));
    }
}
