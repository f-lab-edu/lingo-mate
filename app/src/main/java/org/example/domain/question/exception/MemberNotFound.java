package org.example.domain.question.exception;

import org.example.domain.global.exception.GlobalException;
import org.springframework.http.HttpStatus;

public class MemberNotFound extends GlobalException {
    public MemberNotFound() {
        super(HttpStatus.NOT_FOUND, "MEMBER_NOT_FOUND", "Member not found");
    }
}
