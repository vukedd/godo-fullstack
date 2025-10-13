package com.app.godo.services.review;

import com.app.godo.dtos.review.CreateReviewDto;
import com.app.godo.enums.ReviewStatus;
import com.app.godo.exceptions.general.NotFoundException;
import com.app.godo.exceptions.general.UnauthorizedException;
import com.app.godo.models.*;
import com.app.godo.repositories.event.EventRepository;
import com.app.godo.repositories.review.ReviewRepository;
import com.app.godo.repositories.user.UserRepository;
import com.app.godo.repositories.venue.VenueRepository;
import com.app.godo.services.auth.AuthService;
import com.app.godo.utils.Utils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final VenueRepository venueRepository;
    private final EventRepository eventRepository;

    private final Utils utils;

    private static final Logger logger = LogManager.getLogger(ReviewService.class);

    @Transactional
    public void createReview(CreateReviewDto createReviewDto, String token) {
        logger.info("Create review process has begun");

        logger.info("validating request, comparing sender with token subject");

        if (!utils.extractSubject(token).equals(createReviewDto.getReviewer())) {
            logger.warn("Request sender is different than reviewer, review creation failed");
            throw new UnauthorizedException("you are not allowed to perform this action");
        }

        User reviewer;
        Event event;
        Venue venue;

        try {
            logger.info("Finding user with the username: {}", createReviewDto.getReviewer());
            reviewer = userRepository.findByUsername((createReviewDto.getReviewer()))
                    .orElseThrow(() -> new NotFoundException("the user you were looking for couldn't be found"));

            logger.info("User has been found");

        } catch (NotFoundException ex) {
            logger.warn(ex.getMessage());
            throw new NotFoundException(ex.getMessage());
        }

        try {
            logger.info("finding event with the Id: {}", createReviewDto.getEventId());
            event = eventRepository.findById(createReviewDto.getEventId())
                    .orElseThrow(() -> new NotFoundException("the event you were looking for couldn't be found"));

            logger.info("Event has been found");
        } catch (NotFoundException ex) {
            logger.warn(ex.getMessage());
            throw new NotFoundException(ex.getMessage());
        }

        try {
            logger.info("Finding venue with the Id: {}", createReviewDto.getVenueId());
            venue = venueRepository.findById(createReviewDto.getVenueId())
                    .orElseThrow(() -> new NotFoundException("the venue you were looking for couldn't be found"));

            logger.info("Venue has been found");
        } catch (NotFoundException ex) {
            logger.warn(ex.getMessage());
            throw new NotFoundException(ex.getMessage());
        }

        logger.info("initializing comment entity");
        Comment rootComment = Comment.builder()
                .content(createReviewDto.getComment())
                .createdAt(LocalDate.now())
                .commentedBy(reviewer)
                .build();

        logger.info("initializing rating entity");
        Rating rating = Rating.builder()
                .performance(createReviewDto.getPerformanceGrade())
                .ambient(createReviewDto.getAmbientGrade())
                .venue(createReviewDto.getVenueGrade())
                .overallImpression(createReviewDto.getOverallImpression())
                .build();

        logger.info("initializing review entity");
        Review review = Review.builder()
                .status(ReviewStatus.ACTIVE)
                .createdAt(LocalDate.now())
                .reviewedBy(reviewer)
                .event(event)
                .venue(venue)
                .rootComment(rootComment)
                .rating(rating)
                .build();

        rating.setReview(review);

        logger.info("saving review entity");
        Review newReview = reviewRepository.save(review);

        logger.info("review has been created with the Id: {}", newReview.getId());

    }

}
