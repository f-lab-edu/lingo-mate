package org.example.domain.login;

import org.example.domain.login.dto.LoginForm;
import org.example.domain.login.dto.LoginResponse;
import org.example.domain.member.dto.MemberForm;

import java.util.List;

public class LoginTestFixture {

    public static LoginForm createValidLoginForm() {
        return LoginForm.builder()
                .email("valid@example.com")  // 유효한 이메일
                .password("securePassword123")  // 비밀번호
                .build();
    }

    public static LoginResponse loginSuccessResponse() {
        return new LoginResponse("session123", 1L, "validUsername", "valid@example.com");
    }

    public static LoginForm createInvalidLoginForm() {
        return LoginForm.builder()
                .email("invalid-email")  // 잘못된 이메일 형식
                .password("worng-password")  // 비어 있는 비밀번호
                .build();
    }

    public static LoginResponse loginFailResponse() {
        return new LoginResponse();
    }
}
