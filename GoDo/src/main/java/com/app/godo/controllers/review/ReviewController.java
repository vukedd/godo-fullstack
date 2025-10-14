package com.app.godo.controllers.review;

import com.app.godo.dtos.review.CreateReviewDto;
import com.app.godo.dtos.review.RatingOverviewDto;
import com.app.godo.dtos.review.ReviewOverviewDto;
import com.app.godo.services.review.ReviewService;
import com.app.godo.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review")
public class ReviewController {
    private final ReviewService reviewService;
    private final Utils utils;

    @PostMapping
    public ResponseEntity<?> createReview(
            @RequestBody CreateReviewDto dto,
            @RequestHeader("Authorization") String authHeader
    ) {
        reviewService.createReview(dto, utils.extractToken(authHeader));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{venueId}")
    public ResponseEntity<List<ReviewOverviewDto>> findReviewsByVenueId(@PathVariable("venueId") long venueId) {
        return ResponseEntity.ok(reviewService.findReviewsByVenueId(venueId));
    }

    @GetMapping("/overview/{venueId}")
    public ResponseEntity<RatingOverviewDto> findReviewOverview(@PathVariable("venueId") long venueId) {
        return ResponseEntity.ok(reviewService.findRatingOverviewByVenueId(venueId));
    }

    @GetMapping("/venue/{venueId}")
    public ResponseEntity<Page<ReviewOverviewDto>> findReviewsByVenueIdPage(
            @PathVariable("venueId") long venueId,
            @PageableDefault(size = 5, sort = "createdAt", direction = Sort.Direction.DESC) Pageable eventPage
    ) {
        return ResponseEntity.ok(reviewService.findReviewPageByVenueId(venueId, eventPage));
    }

    @PatchMapping("/hide/{reviewId}")
    public ResponseEntity<Void> hideReview(@PathVariable("reviewId") long reviewId, @RequestHeader("Authorization") String authHeader) {
        reviewService.hideReview(reviewId, utils.extractToken(authHeader));
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/delete/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable("reviewId") long reviewId, @RequestHeader("Authorization") String authHeader) {
        reviewService.deleteReview(reviewId, utils.extractToken(authHeader));
        return ResponseEntity.ok().build();
    }
}
