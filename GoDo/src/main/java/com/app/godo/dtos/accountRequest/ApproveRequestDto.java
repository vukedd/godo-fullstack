package com.app.godo.dtos.accountRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ApproveRequestDto {
    private long requestId;
}
