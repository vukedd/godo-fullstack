package com.app.godo.dtos.event;

import com.app.godo.enums.EventType;
import com.app.godo.models.Event;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventDetailsDto {
    private String name;
    private String description;
    private LocalDate date;
    private EventType type;
    private double price;
    private String imagePath;
    private boolean recurrent;
    private long venueId;


    public static EventDetailsDto fromEntity(Event event) {
        return new EventDetailsDto(
                event.getName(),
                event.getDescription(),
                event.getDate(),
                event.getEventType(),
                event.getPrice(),
                event.getImage().getPath(),
                event.isRecurrent(),
                event.getVenue().getId()
        );
    }
}
