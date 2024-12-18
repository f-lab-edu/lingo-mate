package org.example.domain.question.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.question.dto.request.CommentEditForm;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class Comment {
    private static Long sequence = 0L;

    private Long id;
    private String comment;
    private String author;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Comment editComment(CommentEditForm commentEditForm){
        this.comment = commentEditForm.getComment();
        this.updatedAt = LocalDateTime.now();
        return this;
    }

    public Comment(String content, String author) {
        this.id = ++sequence;
        this.comment = content;
        this.author = author;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}