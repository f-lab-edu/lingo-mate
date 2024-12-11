package org.example.domain.question.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QuestionEditForm {
    private String question_language;
    private String title;
    private String content;
    private LocalDateTime updatedAt;
    private Integer point;
}
