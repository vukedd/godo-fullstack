package com.app.godo.dtos.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDisplayInfoDto {
    private String username;
    private String email;
}