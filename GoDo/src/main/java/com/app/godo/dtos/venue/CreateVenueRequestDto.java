package com.app.godo.dtos.venue;

import com.app.godo.enums.VenueType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateVenueRequestDto {
    private String name;
    private String description;
    private String address;
    private VenueType type;
}
