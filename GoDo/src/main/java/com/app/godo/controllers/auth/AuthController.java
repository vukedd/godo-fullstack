package com.app.godo.controllers.auth;

import com.app.godo.dtos.accountRequest.AccountRequestDto;
import com.app.godo.dtos.accountRequest.AccountRequestSuccessDto;
import com.app.godo.dtos.auth.AuthenticationRequestDto;
import com.app.godo.dtos.auth.AuthenticationResponseDto;
import com.app.godo.services.auth.AuthService;
import com.nimbusds.openid.connect.sdk.AuthenticationResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AccountRequestSuccessDto> createAccountRequest(@Valid @RequestBody AccountRequestDto accountRequest){
        return ResponseEntity.ok(authService.sendRegistrationRequest(accountRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDto> authenticate(@Valid @RequestBody AuthenticationRequestDto authRequest){
        return ResponseEntity.ok(authService.authenticate(authRequest));
    }
}
