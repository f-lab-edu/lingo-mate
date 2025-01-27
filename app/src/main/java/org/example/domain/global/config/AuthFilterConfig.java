package org.example.domain.global.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.auth.jwt.JWTLoginFilter;
import org.example.domain.auth.jwt.JWTUtil;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class AuthFilterConfig {
    private final JWTUtil jwtUtil;
    @Bean
    public FilterRegistrationBean<JWTLoginFilter> jwtLoginFilter() {
        FilterRegistrationBean<JWTLoginFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new JWTLoginFilter(jwtUtil));
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(1);
        return registrationBean;
    }
}
