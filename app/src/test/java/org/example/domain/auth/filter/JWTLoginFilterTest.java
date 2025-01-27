package org.example.domain.auth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.example.domain.auth.AuthService;
import org.example.domain.auth.exception.AccessTokenNotFound;
import org.example.domain.auth.exception.AuthorizationInfoNotExist;
import org.example.domain.auth.exception.TokenExpired;
import org.example.domain.auth.jwt.JWTLoginFilter;
import org.example.domain.auth.jwt.JWTUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JWTLoginFilterTest {

    private JWTLoginFilter jwtLoginFilter;

    @Mock
    private JWTUtil jwtUtil;

    @Mock
    private FilterChain filterChain;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    @Mock
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtLoginFilter = new JWTLoginFilter(jwtUtil,authService);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }


    @Test
    void auth경로는_JWTLoginFilter를_통과한다 () throws ServletException, IOException {

        //Given
        request.setRequestURI("/auth/login");

        //When
        jwtLoginFilter.doFilter(request,response,filterChain);

        //Then
        verify(filterChain, times(1)).doFilter(request,response);

    }

    @Test
    void api로_시작하는_요청에_인증정보가_없으면_AuthorizationInfoNotExist가_발생한다 () throws ServletException, IOException {

        //Given
        request.setRequestURI("/api/resource");

        //When & Then
        assertThrows(AuthorizationInfoNotExist.class, () -> jwtLoginFilter.doFilter(request,response,filterChain));
    }

    @Test
    void api로_시작하는_요청에_authorization헤더_값이_Bearer로_시작하지_않으면_AuthorizationInfoNotExist가_발생한다 () throws ServletException, IOException {
        //Given - Bearer로 시작하지 않은 Authorization 헤더
        request.setRequestURI("/api/resource");
        request.addHeader("Authorization", "Bear");

        //When & Then
        assertThrows(AuthorizationInfoNotExist.class, () -> jwtLoginFilter.doFilter(request,response,filterChain));
    }

    @Test
    void api로_시작하는_요청의_토큰이_만료되었다면_TokenExpired가_발생한다 () throws ServletException, IOException {
        //Given
        request.setRequestURI("/api/resource");
        request.addHeader("Authorization", "Bearer expired");
        when(jwtUtil.isExpired("expired")).thenReturn(true);

        //When & Then
        assertThrows(TokenExpired.class, () -> jwtLoginFilter.doFilter(request,response,filterChain));
    }

    @Test
    void api로_시작하는_요청의_엑세스토큰이_DB에_존재하지_않으면_AccessTokenNotFound가_발생한다(){
        //Given
        request.setRequestURI("/api/resource");
        request.addHeader("Authorization", "Bearer expired");

        //when
        when(authService.isValidAccessToken(any(String.class))).thenReturn(false);

        //then
        assertThrows(AccessTokenNotFound.class, () -> jwtLoginFilter.doFilter(request, response, filterChain));
    }

    @Test
    void api로_시작하는_요청의_토큰이_유효하다면_JWTLoginFilter를_통과한다 () throws ServletException, IOException {
        // given
        request.setRequestURI("/api/resource");
        request.addHeader("Authorization", "Bearer validToken");

        //when
        when(jwtUtil.isExpired("validToken")).thenReturn(false);
        when(authService.isValidAccessToken(any(String.class))).thenReturn(true);

        jwtLoginFilter.doFilter(request, response, filterChain);

        // 필터 체인이 실행되었는지 확인
        verify(filterChain, times(1)).doFilter(request, response);
        assertEquals(200, response.getStatus());

    }
}