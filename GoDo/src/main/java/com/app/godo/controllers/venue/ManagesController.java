package com.app.godo.controllers.venue;

import com.app.godo.dtos.manages.ManagesOverviewDto;
import com.app.godo.dtos.user.UserManagerOptionDto;
import com.app.godo.services.venue.ManagesService;
import com.app.godo.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/manages")
public class ManagesController {
    private final ManagesService managesService;
    private final Utils utils;

    @GetMapping("manager/{username}")
    public ResponseEntity<List<ManagesOverviewDto>> getManagementByUsername(@PathVariable String username) {
        return ResponseEntity.ok(managesService.getManagementByUsername(username));
    }

    @GetMapping("venue/{venueId}")
    public ResponseEntity<List<ManagesOverviewDto>> getManagementByVenueId(@PathVariable long venueId) {
        return ResponseEntity.ok(managesService.getManagementByVenueId(venueId));
    }

    @GetMapping("venue/{venueId}/options")
    public ResponseEntity<List<UserManagerOptionDto>> getManagementSelectedOptionsByVenueId(
            @PathVariable long venueId,
            @RequestHeader("Authorization") String authHeader
    ) {
        return ResponseEntity.ok(managesService.getManagersByVenueId(venueId, utils.extractToken(authHeader)));
    }

    @PutMapping("venue/update/{venueId}")
    public ResponseEntity<Void> updateManagementForVenue(
            @PathVariable long venueId,
            @RequestBody List<UserManagerOptionDto> managerOptionDtos,
            @RequestHeader("Authorization") String authHeader
    ) {
        managesService.updateManagersByVenueId(venueId, managerOptionDtos, utils.extractToken(authHeader));
        return ResponseEntity.noContent().build();
    }
}