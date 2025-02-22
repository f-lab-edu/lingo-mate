package org.example.domain.auth;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assert;
import org.assertj.core.api.Assertions;
import org.example.domain.auth.dto.request.LoginRequest;
import org.example.domain.auth.dto.request.RefreshRequest;
import org.example.domain.auth.dto.response.TokenResponse;
import org.example.domain.auth.entity.AuthEntity;
import org.example.domain.auth.fixture.AuthTestFixture;
import org.example.domain.auth.jwt.JWTUtil;
import org.example.domain.global.exception.GlobalException;
import org.example.domain.member.MemberRepository;
import org.example.domain.member.MemberTestFixture;
import org.example.domain.member.entity.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

//@SpringBootTest
//@Transactional
@Slf4j
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @InjectMocks
    private AuthService authService;
    @Mock
    MemberRepository memberRepository;
    @Mock
    private AuthRepository authRepository;
    @Mock
    private JWTUtil jwtUtil;

    @Test
    void 로그인_했을_때_refreshToken이_DB에_저장되었는지_확인한다() {
        // Given
        Member member = MemberTestFixture.createMember();
        LoginRequest loginRequest = AuthTestFixture.createLoginRequest();
        String generatedAccessToken = AuthTestFixture.createAccessToken();
        String generatedRefreshToken = AuthTestFixture.createRefreshToken();
        AuthEntity authEntity = AuthTestFixture.createAuthEntity();


        // mocking
        // when(memberRepository.save(any(Member.class))).thenReturn(member);
        when(memberRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(member));
        when(jwtUtil.createAccessToken(any(), eq(member.getUsername()), eq(member.getRole())))
                .thenReturn(generatedAccessToken);
        when(jwtUtil.createRefreshToken(any(), eq(member.getUsername()), eq(member.getRole())))
                .thenReturn(generatedRefreshToken);
        when(authRepository.save(any(AuthEntity.class))).thenReturn(authEntity);
        when(authRepository.findByRefreshToken(generatedRefreshToken)).thenReturn(Optional.of(authEntity));


        // When
        TokenResponse tokenResponse = authService.issueToken(loginRequest);
        // Then
        String refreshToken = tokenResponse.getRefreshToken();
        assertThat(authRepository.findByRefreshToken(refreshToken).get()).isNotNull();
    }
    @Test
    void refreshToken과_accessToken이_유효하고_사용자ID가_일치하면_새로운_refresh토큰을_발급한다 () {
        // Given
        Member member = MemberTestFixture.createMember();
        LoginRequest loginRequest = AuthTestFixture.createLoginRequest();
        String generatedAccessToken = AuthTestFixture.createAccessToken();
        String generatedRefreshToken = AuthTestFixture.createRefreshToken();
        RefreshRequest refreshRequest = AuthTestFixture.createRefreshRequest();
        AuthEntity authEntity = AuthTestFixture.createAuthEntity();

        String newAccessToken = "newAccessToken";
        String newRefreshToken = "newRefreshToken";

        //mocking
        when(jwtUtil.isExpired(refreshRequest.getRefreshToken())).thenReturn(false);
        when(jwtUtil.getId(generatedAccessToken)).thenReturn(1L);
        when(jwtUtil.getId(generatedRefreshToken)).thenReturn(1L);
        when(authRepository.findByRefreshToken(generatedRefreshToken)).thenReturn(Optional.of(authEntity));
        when(memberRepository.findById(any())).thenReturn(Optional.of(member));
        when(jwtUtil.createAccessToken(any(),eq(member.getUsername()),eq(member.getRole()))).thenReturn(newAccessToken);
        doNothing().when(authRepository).deleteByAuthId(any());
        when(jwtUtil.createRefreshToken(any(),eq(member.getUsername()),eq(member.getRole()))).thenReturn(newRefreshToken);
        when(authRepository.save(any(AuthEntity.class))).thenReturn(authEntity);


        //when
        TokenResponse tokenResponse = authService.reissueRefreshToken(refreshRequest);

        //Then
        Assertions.assertThat(tokenResponse.getRefreshToken()).isEqualTo(newRefreshToken);
    }

    @Test
    void refreshToken이_만료되었다면_TokenExpired가_발생한다() {
        // Given
        RefreshRequest refreshRequest = AuthTestFixture.createRefreshRequest();

        // mocking
        when(jwtUtil.isExpired(refreshRequest.getRefreshToken())).thenThrow(ExpiredJwtException.class);
        doNothing().when(authRepository).deleteByRefreshToken(refreshRequest.getRefreshToken());

        // When
        GlobalException globalException = assertThrows(GlobalException.class, () -> authService.reissueRefreshToken(refreshRequest));
        assertEquals("TOKEN_EXPIRED", globalException.getName());

    }

    @Test
    void accssToken과_refreshToken의_사용자ID가_일치하지_않는다면_401에러를_발생시킨다(){
        // Given
        RefreshRequest refreshRequest = AuthTestFixture.createRefreshRequest();

        // mocking
        when(jwtUtil.isExpired(refreshRequest.getRefreshToken())).thenReturn(false);
        when(jwtUtil.getId(refreshRequest.getAccessToken())).thenReturn(1L);
        when(jwtUtil.getId(refreshRequest.getRefreshToken())).thenReturn(2L);
        // When
        GlobalException globalException = assertThrows(GlobalException.class, () -> authService.reissueRefreshToken(refreshRequest));

        // Then
        assertEquals("TOKEN_IDS_MISMATCH_EXCEPTION", globalException.getName());

    }
}