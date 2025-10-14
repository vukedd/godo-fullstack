package com.app.godo.controllers.comment;

import com.app.godo.dtos.comment.ReplyRequestDto;
import com.app.godo.services.comment.CommentService;
import com.app.godo.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    private final Utils utils;

    @PostMapping("/reply")
    public ResponseEntity<?> replyOnReview(
            @RequestBody ReplyRequestDto request,
            @RequestHeader("Authorization") String authHeader
    ) {
        commentService.managerReply(request.getReviewId(), request.getCommentContent(), utils.extractToken(authHeader));
        return ResponseEntity.ok().build();
    }
}
