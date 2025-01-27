package org.example.domain.auth.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.auth.AuthService;
import org.example.domain.auth.AuthenticationContext;
import org.example.domain.auth.exception.AccessTokenNotFound;
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
        log.debug("interceptor work");

        String accessToken = request.getHeader("Authorization").substring(7);

        Long acceptedMemberId = jwtUtil.getId(accessToken);

        authenticationContext.setAuthentication(acceptedMemberId);
        return true;
    }
}
