package org.example.domain.question.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class QuestionCreateForm {
    private String author;
    private String question_language;
    private String title;
    private String content;
    private Integer point;
}
