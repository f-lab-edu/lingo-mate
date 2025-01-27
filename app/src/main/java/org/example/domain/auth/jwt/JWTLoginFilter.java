package org.example.domain.auth.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.auth.AuthService;
import org.example.domain.auth.exception.AccessTokenNotFound;
import org.example.domain.auth.exception.AuthorizationInfoNotExist;
import org.example.domain.auth.exception.TokenExpired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JWTLoginFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final AuthService authService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.debug("jwt filter work");
        String requestURI = request.getRequestURI();

        // /auth 경로는 로그인 필터를 적용하지 않는다.
        if(requestURI.startsWith("/auth/")) {
            filterChain.doFilter(request,response);
            return;
        }

        String authorization = request.getHeader("Authorization");


        // /api 경로의 요청 헤더 인증 정보 검사
        if(authorization == null || !authorization.startsWith("Bearer ")) {
            throw new AuthorizationInfoNotExist();
        }

        String token = authorization.split(" ")[1];

        // AccessToken 만료 검사
        if (jwtUtil.isExpired(token)) {
           throw new TokenExpired();
        }

        // DB에서 AccessToken 검증
        boolean isTokenValid = authService.isValidAccessToken(token);
        if (!isTokenValid) {
            log.debug("Token not found in DB or invalid");
            throw new AccessTokenNotFound();
        }

        // 인증 정보가 유효함. 다음 필터로 이동
        filterChain.doFilter(request,response);

    }
}
