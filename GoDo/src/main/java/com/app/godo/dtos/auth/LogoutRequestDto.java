package com.app.godo.dtos.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LogoutRequestDto {
    public long refreshTokenId;
}
