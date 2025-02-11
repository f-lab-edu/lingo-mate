package org.example.domain.auth.exception;

import org.example.domain.global.exception.GlobalException;
import org.springframework.http.HttpStatus;

public class TokenExpired extends GlobalException {
    public TokenExpired() {
        super(HttpStatus.UNAUTHORIZED, "TOKEN_EXPIRED", "토큰의 유효기간이 만료되었습니다.");
    }
}
