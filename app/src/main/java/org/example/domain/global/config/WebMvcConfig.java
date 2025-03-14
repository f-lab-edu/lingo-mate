package org.example.domain.global.config;

import lombok.RequiredArgsConstructor;
import org.example.domain.auth.interceptor.LoginCheckInterceptor;
import org.example.domain.auth.resolver.LoginMemberArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final LoginMemberArgumentResolver loginMemberArgumentResolver;
    private final LoginCheckInterceptor loginCheckInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginCheckInterceptor)
                .order(1)
                .addPathPatterns("/api/**")
                .excludePathPatterns("/auth/**","/api/question/all", "/api/wordbook/all");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginMemberArgumentResolver);
    }


}
