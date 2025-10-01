package com.app.godo.controllers.venue;

import com.app.godo.dtos.manages.ManagesOverviewDto;
import com.app.godo.services.venue.ManagesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/manages")
public class ManagesController {
    private final ManagesService managesService;

    @GetMapping("manager/{username}")
    public ResponseEntity<List<ManagesOverviewDto>> getManagementByUsername(@PathVariable String username) {
        return ResponseEntity.ok(managesService.getManagementByUsername(username));
    }

    @GetMapping("venue/{venueId}")
    public ResponseEntity<List<ManagesOverviewDto>> getManagementByVenueId(@PathVariable long venueId) {
        return ResponseEntity.ok(managesService.getManagementByVenueId(venueId));
    }
}