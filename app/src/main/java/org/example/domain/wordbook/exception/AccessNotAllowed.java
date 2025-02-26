package org.example.domain.wordbook.exception;

import org.example.domain.global.exception.GlobalException;
import org.springframework.http.HttpStatus;

public class AccessNotAllowed extends GlobalException {
    public AccessNotAllowed() {
        super(HttpStatus.UNAUTHORIZED, "ACCESS_NOT_ALLOWED", "AccessNotAllowed");
    }
}
