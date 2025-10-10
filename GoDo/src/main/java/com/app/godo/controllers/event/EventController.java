package com.app.godo.controllers.event;

import com.app.godo.dtos.event.CreateEventRequestDto;
import com.app.godo.services.event.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/event")
public class EventController {
    private final EventService eventService;

    @PostMapping("/{venueId}")
    public ResponseEntity<CreateEventRequestDto> addEvent(
            @PathVariable("venueId") Long venueId,
            @RequestPart("event") String eventJson,
            @RequestPart("image") MultipartFile imageFile
    ) {
        CreateEventRequestDto dto = eventService.convertToCreateEventRequest(eventJson);
        return ResponseEntity.ok(eventService.createEvent(venueId, dto, imageFile));
    }
}
