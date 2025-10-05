package com.app.godo.dtos.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class EditUserProfileDto {
    private String phoneNumber;
    private String city;
    private String address;
    private LocalDate dateOfBirth;
}
