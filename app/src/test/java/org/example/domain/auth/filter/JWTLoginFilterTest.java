package org.example.domain.auth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtLoginFilter = new JWTLoginFilter(jwtUtil);
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
    void api경로는_인증_정보가_없으면_JWTLoginFilter에서_걸리고_401을_리턴한다 () throws ServletException, IOException {

        //Given
        request.setRequestURI("/api/resource");

        //When
        jwtLoginFilter.doFilter(request,response,filterChain);

        //Then
        verify(filterChain, never()).doFilter(request,response);
        assertEquals(401, response.getStatus());
    }

    @Test
    void api경로의_authorization이_Bearer로_시작하지_않으면_JWTLoginFilter에서_걸리고_401을_리턴한다 () throws ServletException, IOException {
        //Given - Bearer로 시작하지 않은 Authorization 헤더
        request.setRequestURI("/api/resource");
        request.addHeader("Authorization", "Bear");

        //When
        jwtLoginFilter.doFilter(request,response,filterChain);

        //Then
        verify(filterChain, never()).doFilter(request,response);
        assertEquals(401, response.getStatus());
    }

    @Test
    void api경로의_토큰이_만료되었다면_JWTLoginFilter를_통과하지_못한다 () throws ServletException, IOException {
        //Given
        request.setRequestURI("/api/resource");
        request.addHeader("Authorization", "Bearer expired");
        when(jwtUtil.isExpired("expired")).thenReturn(true);

        //When
        jwtLoginFilter.doFilter(request,response,filterChain);

        //Then
        verify(filterChain, never()).doFilter(request,response);
        assertEquals(401, response.getStatus());
    }

    @Test
    void api경로의_토큰이_유효하면_JWTLoginFilter를_통과한다 () throws ServletException, IOException {
        // 유효한 토큰
        request.setRequestURI("/api/resource");
        request.addHeader("Authorization", "Bearer validToken");
        when(jwtUtil.isExpired("validToken")).thenReturn(false);

        jwtLoginFilter.doFilter(request, response, filterChain);

        // 필터 체인이 실행되었는지 확인
        verify(filterChain, times(1)).doFilter(request, response);
        assertEquals(200, response.getStatus());

    }
}