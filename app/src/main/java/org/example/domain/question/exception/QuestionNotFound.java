package org.example.domain.question.exception;

import org.example.domain.global.exception.GlobalException;
import org.springframework.http.HttpStatus;

public class QuestionNotFound extends GlobalException {
    public QuestionNotFound() {
        super(HttpStatus.NOT_FOUND, "QUESTION_NOT_FOUND", "해당 질문글이 존재하지 않습니다.");
    }
}
