package com.app.godo.dtos.comment;

import com.app.godo.models.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private String content;
    private LocalDate createdAt;

    public static CommentDto fromEntity(Comment comment) {
        if (comment == null) {
            return null;
        }
        return CommentDto.builder()
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
