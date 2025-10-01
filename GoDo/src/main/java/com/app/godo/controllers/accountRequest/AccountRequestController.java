package com.app.godo.controllers.accountRequest;

import com.app.godo.dtos.accountRequest.*;
import com.app.godo.services.accountRequest.AccountRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/account-request")
@RequiredArgsConstructor
public class AccountRequestController {
    private final AccountRequestService accountRequestService;

    @GetMapping("/pending")
    public ResponseEntity<List<PendingAccountRequestDto>> getPendingRequests() {
        return ResponseEntity.ok(accountRequestService.getPendingAccountRequests());
    }

    @PostMapping("/approve")
    public ResponseEntity<ApprovedRegistrationRequestDto> approvePendingRequest(@RequestBody ApproveRequestDto approveRequest) {
        return ResponseEntity.ok(accountRequestService.approveRequest(approveRequest.getRequestId()));
    }

    @PutMapping("/reject")
    public ResponseEntity<RejectRegistrationRequestDto> rejectPendingRequest(@RequestBody RejectRequestDto approveRequest) {
        return ResponseEntity.ok(accountRequestService.rejectRequest(approveRequest));
    }
}
