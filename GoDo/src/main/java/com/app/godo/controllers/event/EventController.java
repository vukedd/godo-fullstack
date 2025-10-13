package com.app.godo.controllers.event;

import com.app.godo.dtos.event.CreateEventRequestDto;
import com.app.godo.dtos.event.EventDetailsDto;
import com.app.godo.dtos.event.EventReviewOptionDto;
import com.app.godo.dtos.event.UpcomingEventDto;
import com.app.godo.dtos.venue.VenueOverviewDto;
import com.app.godo.services.event.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/event")
public class EventController {
    private final EventService eventService;

    @GetMapping("/review-options/{id}")
    public ResponseEntity<List<EventReviewOptionDto>> getEventReviewOptions(@PathVariable("id") long venueId) {
        return ResponseEntity.ok(eventService.findEventReviewOptions(venueId));
    }

    @GetMapping("/today")
    public ResponseEntity<List<EventDetailsDto>> findEventsHappeningToday() {
        return ResponseEntity.ok(eventService.findEventsHappeningToday());
    }

    @GetMapping
    public ResponseEntity<?> filterEvents (
            @RequestParam(value = "filter", defaultValue = "") String filter,
            @RequestParam(value = "priceFrom", defaultValue = "-1") double priceFrom,
            @RequestParam(value = "priceTo", defaultValue = "-1") double priceTo,
            @RequestParam(value = "eventType", defaultValue = "-1") int venueType,
            @RequestParam(value = "date", required = false) LocalDate eventDate,
            @PageableDefault(size = 8, sort = "date", direction = Sort.Direction.ASC) Pageable eventPage
    ){
        return ResponseEntity.ok(eventService.filterEvents(filter, priceFrom, priceTo, venueType, eventDate, eventPage));
    }

    @PostMapping("/{venueId}")
    public ResponseEntity<CreateEventRequestDto> addEvent(
            @PathVariable("venueId") Long venueId,
            @RequestPart("event") String eventJson,
            @RequestPart("image") MultipartFile imageFile
    ) {
        CreateEventRequestDto dto = eventService.convertToCreateEventRequest(eventJson);
        return ResponseEntity.ok(eventService.createEvent(venueId, dto, imageFile));
    }

    @GetMapping("/upcoming/{venueId}")
    public ResponseEntity<List<UpcomingEventDto>> findUpcomingEventsByVenue(
            @PathVariable("venueId") Long venueId
    ) {
        return ResponseEntity.ok(eventService.findAllUpcomingEventsByVenueId(venueId));
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventDetailsDto> findEventById(
            @PathVariable("eventId") long eventId
    ) {
        return ResponseEntity.ok(eventService.findEventById(eventId));
    }

}
