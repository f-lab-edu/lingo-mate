package org.example.domain.auth.exception;

import org.example.domain.global.exception.GlobalException;
import org.springframework.http.HttpStatus;

public class RefreshTokenNotFound extends GlobalException {
    public RefreshTokenNotFound() {
        super(HttpStatus.NOT_FOUND, "REFRESH_TOKEN_NOT_EXIST", "존재하지 않는 refreshToken 입니다");
    }
}
