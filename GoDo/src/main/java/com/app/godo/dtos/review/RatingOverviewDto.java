package com.app.godo.dtos.review;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RatingOverviewDto {
    private int reviewCount;
    private double averageRating;
}
