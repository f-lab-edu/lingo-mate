package org.example.helper;

import org.example.domain.auth.AuthService;
import org.example.domain.auth.AuthenticationContext;
import org.example.domain.auth.interceptor.LoginCheckInterceptor;
import org.example.domain.auth.jwt.JWTLoginFilter;
import org.example.domain.auth.jwt.JWTUtil;
import org.example.domain.auth.resolver.LoginMemberArgumentResolver;
import org.example.domain.member.MemberService;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

public class MockBeanInjection {
    //auth
    @MockitoBean
    protected JWTUtil jwtUtil;
    @MockitoBean
    protected AuthenticationContext authenticationContext;
    @MockitoBean
    protected AuthService authService;
    @MockitoBean
    protected MemberService memberService;

}
