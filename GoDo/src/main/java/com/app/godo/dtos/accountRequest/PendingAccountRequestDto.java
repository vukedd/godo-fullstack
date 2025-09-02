package com.app.godo.dtos.accountRequest;

import com.app.godo.enums.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class PendingAccountRequestDto {
    private long id;
    private String username;
    private String email;
    private LocalDateTime submittedAt;
}
