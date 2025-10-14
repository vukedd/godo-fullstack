package com.app.godo.repositories.review;

import com.app.godo.enums.ReviewStatus;
import com.app.godo.models.Review;
import com.app.godo.models.Venue;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByVenueAndStatus(Venue venue, ReviewStatus status);

    Page<Review> findByVenueAndStatus(Venue venue, ReviewStatus status, Pageable pageable);

    List<Review> findReviewByVenue(Venue venue);
}
