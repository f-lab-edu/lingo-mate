package org.example.domain.auth.resolver;

import jakarta.validation.constraints.AssertTrue;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.example.domain.auth.AuthenticationContext;
import org.example.domain.auth.annotation.LoginMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import static org.mockito.Mockito.when;

@Slf4j
class LoginMemberArgumentResolverTest {
    private LoginMemberArgumentResolver resolver;

    @Mock
    AuthenticationContext authenticationContext;
    @Mock
    MethodParameter methodParameter;

    @Mock
    ModelAndViewContainer modelAndViewContainer;

    @Mock
    NativeWebRequest nativeWebRequest;
    @Mock
    WebDataBinderFactory webDataBinderFactory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        resolver = new LoginMemberArgumentResolver(authenticationContext);
    }
    @Test
    void LoginMember_어노테이션과_문자열_타입을_만족하는지_확인한다() {
        // Given
        when(methodParameter.hasParameterAnnotation(LoginMember.class)).thenReturn(true);
        when(methodParameter.getParameterType()).thenReturn((Class) Long.class);

        // When
        boolean result = resolver.supportsParameter(methodParameter);

        // Then
        Assertions.assertThat(result).isTrue();



    }
    @Test
    void resolveArgument는_authentication_context의_principal값을_리턴한다() throws Exception {
        //Given
        when(authenticationContext.getPrincipal()).thenReturn(1L);

        //When
        Long result = (Long)resolver.resolveArgument(methodParameter, modelAndViewContainer, nativeWebRequest, webDataBinderFactory);

        //Then
        Assertions.assertThat(result).isEqualTo(1L);
    }

}