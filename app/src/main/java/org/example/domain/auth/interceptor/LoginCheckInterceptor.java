package org.example.domain.auth.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.auth.AuthenticationContext;
import org.example.domain.auth.jwt.JWTUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


@Component
@RequiredArgsConstructor
@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {
    private final JWTUtil jwtUtil;
    private final AuthenticationContext authenticationContext;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.debug("interceptor");

        String token = request.getHeader("Authorization");
        token = token.substring(7);
        String username = jwtUtil.getUsername(token);
        log.debug("interceptor -> token = {}", token);

        authenticationContext.setAuthentication(username);
        return true;
    }
}
