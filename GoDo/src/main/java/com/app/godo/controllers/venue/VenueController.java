package com.app.godo.controllers.venue;


import com.app.godo.dtos.venue.CreateVenueRequestDto;
import com.app.godo.dtos.venue.VenueOverviewDto;
import com.app.godo.models.Venue;
import com.app.godo.services.venue.VenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/venue")
@RequiredArgsConstructor
public class VenueController {
    private final VenueService venueService;

    @GetMapping
    public ResponseEntity<Page<VenueOverviewDto>> filterVenues(
            @RequestParam(value = "name", defaultValue = "") String name,
            @RequestParam(value = "address", defaultValue = "") String address,
            @RequestParam(value = "venueType", defaultValue = "-1") int venueType,
            @PageableDefault(size = 4, sort = "name", direction = Sort.Direction.ASC) Pageable venuePage
    ){
        return ResponseEntity.ok(venueService.filterVenues(name, address, venueType, venuePage));
    }

    @PostMapping(consumes = { "multipart/form-data" })
    public ResponseEntity<VenueOverviewDto> createVenue(
            @RequestPart("venue") String venueJson,
            @RequestPart("image") MultipartFile imageFile) {

        CreateVenueRequestDto createVenueRequest = venueService.convertToCreateVenueRequest(venueJson);
        return ResponseEntity.ok(venueService.createVenue(createVenueRequest, imageFile));
    }
}
