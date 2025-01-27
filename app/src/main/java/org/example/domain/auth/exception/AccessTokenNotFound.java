package org.example.domain.auth.exception;

import org.example.domain.global.exception.GlobalException;
import org.springframework.http.HttpStatus;

public class AccessTokenNotFound extends GlobalException {

    public AccessTokenNotFound(){
        super(HttpStatus.UNAUTHORIZED, "ACCESS_TOKEN_NOT_FOUND", "존재하지 않는 accessToken 입니다.");
    }
}
