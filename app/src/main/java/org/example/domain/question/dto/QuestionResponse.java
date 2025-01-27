package org.example.domain.question.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.question.entity.Question;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class QuestionResponse {
    private String questionLanguage;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int point;

    private QuestionResponse(final Question question) {
        this.questionLanguage = question.getQuestionLanguage();
        this.title = question.getTitle();
        this.content = question.getContent();
        this.createdAt = question.getCreatedAt();
        this.updatedAt = question.getUpdatedAt();
        this.point = question.getPoint();

    }
    public static QuestionResponse create(Question question){
        return new QuestionResponse(question);
    }

}
