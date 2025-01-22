package org.example.helper;

import org.example.domain.auth.AuthService;
import org.example.domain.auth.AuthenticationContext;
import org.example.domain.auth.jwt.JWTUtil;
import org.example.domain.member.MemberService;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
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
