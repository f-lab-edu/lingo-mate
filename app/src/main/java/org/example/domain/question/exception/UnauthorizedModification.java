package org.example.domain.question.exception;

import org.example.domain.global.exception.GlobalException;
import org.springframework.http.HttpStatus;

public class UnauthorizedModification extends GlobalException {
    public UnauthorizedModification(){
        super(HttpStatus.FORBIDDEN, "UNAUTHORIZED_MODIFICATION", "데이터 수정 권한이 없습니다.");
    }
}
