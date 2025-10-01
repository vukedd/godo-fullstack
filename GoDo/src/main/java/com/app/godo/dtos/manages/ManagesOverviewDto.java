package com.app.godo.dtos.manages;

import com.app.godo.models.Manages;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ManagesOverviewDto {
    private long id;
    private String managerUsername;
    private long venueId;

    public static ManagesOverviewDto fromEntity(Manages manages) {
        return new ManagesOverviewDto(
                manages.getId(),
                manages.getManager().getUsername(),
                manages.getVenue().getId()
        );
    }
}


