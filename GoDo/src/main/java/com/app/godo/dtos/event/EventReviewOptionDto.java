package com.app.godo.dtos.event;

import com.app.godo.models.Event;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventReviewOptionDto {
    private long id;
    private String name;

    public static EventReviewOptionDto fromEntity(Event event) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String formattedDate = event.getDate().format(formatter);

        return new EventReviewOptionDto(
                event.getId(),
                event.getName() + " - " + formattedDate
        );
    }
}
