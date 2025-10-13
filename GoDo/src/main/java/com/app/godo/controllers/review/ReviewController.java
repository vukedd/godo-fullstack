package com.app.godo.controllers.review;

import com.app.godo.dtos.review.CreateReviewDto;
import com.app.godo.services.review.ReviewService;
import com.app.godo.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
