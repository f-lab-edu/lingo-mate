package org.example.domain.auth.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.AssertTrue;
import org.assertj.core.api.Assertions;
import org.example.domain.auth.AuthenticationContext;
import org.example.domain.auth.jwt.JWTUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LoginCheckInterceptorTest {
    private LoginCheckInterceptor loginCheckInterceptor;
    private AuthenticationContext authenticationContext;
    @Mock
    JWTUtil jwtUtil;

    @Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @Mock
    Object handler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authenticationContext = new AuthenticationContext();
        loginCheckInterceptor = new LoginCheckInterceptor(jwtUtil, authenticationContext);
    }

    @Test
    void 인증된_사용자의_이름이_authenticationContext에_저장된다( ) throws Exception {
        //Given
        String validToken = "Bearer valid";
        String username = "test";

        when(request.getHeader("Authorization")).thenReturn(validToken);
        when(jwtUtil.getUsername("valid")).thenReturn(username);

        //When
        boolean result = loginCheckInterceptor.preHandle(request, response, handler);

        //Then
        assertTrue(result);
        Assertions.assertThat(authenticationContext.getPrincipal()).isEqualTo("test");
    }
}