package org.example.domain.question.exception;

import org.example.domain.global.exception.GlobalException;
import org.springframework.http.HttpStatus;

public class CommentNotFound extends GlobalException {
    public CommentNotFound() {
        super(HttpStatus.NOT_FOUND, "COMMENT_NOT_FOUND", "해당 댓글은 존재하지 않습니다");
    }
}
