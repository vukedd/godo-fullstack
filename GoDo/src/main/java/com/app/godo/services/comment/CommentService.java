package com.app.godo.services.comment;

import com.app.godo.exceptions.general.ConflictException;
import com.app.godo.exceptions.general.NotFoundException;
import com.app.godo.exceptions.general.UnauthorizedException;
import com.app.godo.models.*;
import com.app.godo.repositories.comment.CommentRepository;
import com.app.godo.repositories.review.ReviewRepository;
import com.app.godo.repositories.user.UserRepository;
import com.app.godo.repositories.venue.ManagesRepository;
import com.app.godo.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ManagesRepository managesRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    private final Utils utils;

    private static final Logger logger = LogManager.getLogger(CommentService.class);

    public void managerReply(long reviewId, String commentContent, String token) {
        String subject = utils.extractSubject(token);
        logger.info("Manager '{}' attempting to reply to review with ID: {}", subject, reviewId);

        Review review;
        try {
            review = reviewRepository
                    .findById(reviewId).orElseThrow(() -> new NotFoundException("The review you were looking for couldn't be found!"));
            logger.debug("Fetched review with ID: {}", reviewId);
        } catch (NotFoundException ex) {
            logger.error("Review not found for ID: {}. Message: {}", reviewId, ex.getMessage());
            throw new NotFoundException(ex.getMessage());
        }

        if (!review.getRootComment().getReplies().isEmpty()) {
            logger.warn("Review with ID {} already has a management reply. Operation aborted.", reviewId);

            throw new ConflictException("Management already handled this review");
        }

        if (managesRepository.findManagement(subject, review.getVenue().getId()).isEmpty()) {
            logger.warn("Unauthorized access attempt by manager '{}' for venue ID {}", subject, review.getVenue().getId());

            throw new UnauthorizedException("You are not allowed to perform this operation!");
        }

        User commentedBy;
        try {
            commentedBy = userRepository.findByUsername(subject)
                    .orElseThrow(() -> new NotFoundException("The user you were looking for couldn't be found!"));
            logger.debug("Found user '{}' who is posting the comment.", subject);

        } catch (NotFoundException ex) {
            logger.error("User '{}' not found in the database. Message: {}", subject, ex.getMessage());

            throw new NotFoundException(ex.getMessage());
        }


        Comment comment = Comment.builder()
                .commentedBy(commentedBy)
                .content(commentContent)
                .createdAt(LocalDate.now())
                .parent(review.getRootComment())
                .build();

        commentRepository.save(comment);
        logger.info("Manager '{}' successfully replied to review ID {} with comment: '{}'", subject, reviewId, commentContent);
    }
}
