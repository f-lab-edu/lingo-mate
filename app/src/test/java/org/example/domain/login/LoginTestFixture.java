package org.example.domain.login;

import org.example.domain.error.ErrorMsg;
import org.example.domain.login.dto.request.LoginForm;
import org.example.domain.login.dto.response.LoginResponse;

public class LoginTestFixture {

    // 로그인 성공 입력 폼
    public static LoginForm createValidLoginForm() {
        return new LoginForm("valid@example.com", "validPassword123");
    }

    // 로그인 실패 입력 폼
    public static LoginForm createInvalidLoginForm() {
        return new LoginForm("valid@example.com", "worng-password");
    }

    // 로그인 성공 응답
    public static LoginResponse loginSuccessResponse() {
        return LoginResponse.builder()
                .sessionId("session_id")
                .userId(1L)
                .username("validUsername")
                .email("valid@example.com")
                .message("login success")
                .build();
    }


}
