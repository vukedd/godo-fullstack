package com.app.godo.dtos.venue;

import com.app.godo.enums.LogicalOperator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VenueFilterDto {
    private String filter; // Existing text search query

    // Review Count Range
    private Integer minReviewCount;
    private Integer maxReviewCount;

    // Ratings Ranges (mapped to VenueDocument fields)
    private Double minRatingPerformance;
    private Double maxRatingPerformance;

    private Double minRatingAmbient;
    private Double maxRatingAmbient;

    private Double minRatingVenue;
    private Double maxRatingVenue;

    private Double minRatingOverall;
    private Double maxRatingOverall;

    // Operator to combine these range fields (Default to AND)
    private LogicalOperator operator = LogicalOperator.AND;
}