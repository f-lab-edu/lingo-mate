package org.example.domain.auth.exception;

import org.example.domain.global.exception.GlobalException;
import org.springframework.http.HttpStatus;

public class AuthorizationInfoNotExist extends GlobalException {
    public AuthorizationInfoNotExist() {
        super(HttpStatus.UNAUTHORIZED, "AUTHORIZATION_INFO_NOT_EXIST", "헤더에 인증 정보가 없거나 형식이 잘못 되었습니다.");
    }
}
