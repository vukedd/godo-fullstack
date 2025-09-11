package com.app.godo.dtos.venue;

import com.app.godo.enums.VenueType;
import com.app.godo.models.Image;
import com.app.godo.models.Venue;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class VenueOverviewDto {
    private long id;
    private String name;
    private String description;
    private String address;
    private double averageRating;
    private VenueType type;
    private String imagePath;

    public static VenueOverviewDto fromEntity(Venue venue) {
        return new VenueOverviewDto(
                venue.getId(),
                venue.getName(),
                venue.getDescription(),
                venue.getAddress(),
                venue.getAverageRating(),
                venue.getType(),
                venue.getImage().getPath()
        );
    }
}
