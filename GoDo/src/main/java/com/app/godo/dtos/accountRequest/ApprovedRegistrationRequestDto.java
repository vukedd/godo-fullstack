package com.app.godo.dtos.accountRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class ApprovedRegistrationRequestDto {
    private String message;
}
