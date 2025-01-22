package org.example.domain.auth.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JWTLoginFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();

        if(requestURI.startsWith("/auth/")) {
            filterChain.doFilter(request,response);
            return;
        }

        String authorization = request.getHeader("Authorization");

        log.debug("v = {}", authorization);
        // "/auth/* 제외 인증 정보 없이 다른 경로 접근시 401 응답 처리
        if(authorization == null || !authorization.startsWith("Bearer ")) {
            // 이 부분 예외처리로
            response.setStatus(401);
            log.debug("/api/* 경로는 인증 정보 필요");
            return;
        }

        String token = authorization.split(" ")[1];

        // AccessToken 만료시 401 응답 처리
        if (jwtUtil.isExpired(token)) {
            // 이 부분 예외처리로
            response.setStatus(401);
            log.debug("토큰 만료");
            return;
        }

        // 인증 정보가 유효함. 다음 필터로 go
        filterChain.doFilter(request,response);

    }
}
