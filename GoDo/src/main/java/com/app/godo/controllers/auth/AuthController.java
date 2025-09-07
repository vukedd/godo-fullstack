package com.app.godo.controllers.auth;

import com.app.godo.dtos.accountRequest.AccountRequestDto;
import com.app.godo.dtos.accountRequest.AccountRequestSuccessDto;
import com.app.godo.dtos.auth.AuthenticationRequestDto;
import com.app.godo.dtos.auth.AuthenticationResponseDto;
import com.app.godo.services.auth.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/refresh-token/{id}")
    public ResponseEntity<AuthenticationResponseDto> refreshToken(@PathVariable Long id) {
        return ResponseEntity.ok(authService.refreshToken(id));
    }}
