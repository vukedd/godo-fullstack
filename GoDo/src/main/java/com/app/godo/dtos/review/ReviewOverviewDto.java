package com.app.godo.dtos.review;

import com.app.godo.dtos.comment.CommentDto;
import com.app.godo.enums.EventType;
import com.app.godo.enums.ReviewStatus;
import com.app.godo.models.Comment;
import com.app.godo.models.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewOverviewDto {
    private long reviewId;
    private String reviewer;
    private String reviewerPfpPath;
    private long eventId;
    private String eventName;
    private ReviewStatus reviewStatus;
    private int eventCount;
    private String comment;
    private int performanceGrade;
    private int venueGrade;
    private int ambientGrade;
    private int overallImpression;
    private CommentDto reviewVenueReply;

    public static ReviewOverviewDto fromEntity(Review review, int eventCount) {

        Comment comment;
        if (review.getRootComment().getReplies().isEmpty()) {
            comment = null;
        } else {
            comment = review.getRootComment().getReplies().getFirst();
        }

        CommentDto dto;
        if (comment != null) {
            dto = new CommentDto(comment.getContent(), comment.getCreatedAt());
        } else {
            dto = null;
        }

        return new ReviewOverviewDto(
                review.getId(),
                review.getReviewedBy().getUsername(),
                review.getReviewedBy().getProfileImage().getPath(),
                review.getEvent().getId(),
                review.getEvent().getName(),
                review.getStatus(),
                eventCount,
                review.getRootComment().getContent(),
                review.getRating().getPerformance(),
                review.getRating().getVenue(),
                review.getRating().getAmbient(),
                review.getRating().getOverallImpression(),
                dto
        );
    }
}
