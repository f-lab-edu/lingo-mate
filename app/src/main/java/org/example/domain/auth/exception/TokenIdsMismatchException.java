package org.example.domain.auth.exception;

import org.example.domain.global.exception.GlobalException;
import org.springframework.http.HttpStatus;

public class TokenIdsMismatchException extends GlobalException {
    public TokenIdsMismatchException() {
        super(HttpStatus.FORBIDDEN, "TOKEN_IDS_MISMATCH_EXCEPTION","AcessToken과 RefreshToken의 id가 일치하지 않습니다");
    }
}
