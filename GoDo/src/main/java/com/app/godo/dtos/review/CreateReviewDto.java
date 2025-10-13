package com.app.godo.dtos.review;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateReviewDto {
    private String reviewer;
    private long venueId;
    private long eventId;
    private String comment;
    private int performanceGrade;
    private int venueGrade;
    private int ambientGrade;
    private int overallImpression;
}
