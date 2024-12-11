package org.example.domain.question.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Comment {
    private static Long sequence = 0L;

    private Long id;
    private String comment;
    private String author;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Comment(String content, String author) {
        this.id = ++sequence;
        this.comment = content;
        this.author = author;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}