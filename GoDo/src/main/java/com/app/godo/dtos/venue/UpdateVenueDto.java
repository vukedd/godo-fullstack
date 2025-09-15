package com.app.godo.dtos.venue;

import com.app.godo.enums.VenueType;
import com.app.godo.models.Venue;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateVenueDto {
    private String name;
    private String description;
    private String address;
    private VenueType venueType;

    public static UpdateVenueDto fromEntity(Venue venue) {
        return new UpdateVenueDto(
                venue.getName(),
                venue.getDescription(),
                venue.getAddress(),
                venue.getType()
        );
    }
}
