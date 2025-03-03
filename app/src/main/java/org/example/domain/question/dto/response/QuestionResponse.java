package org.example.domain.question.dto.response;

import lombok.Getter;
import org.example.domain.question.entity.Question;

import java.time.LocalDateTime;

@Getter
public class QuestionResponse {
    private Long questionId;
    private String questionLanguage;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int point;

    private QuestionResponse(final Question question) {
        this.questionId = question.getId();
        this.questionLanguage = question.getQuestionLanguage();
        this.title = question.getTitle();
        this.content = question.getContent();
        this.point = question.getPoint();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public static QuestionResponse create(final Question question){
        return new QuestionResponse(question);
    }
}
