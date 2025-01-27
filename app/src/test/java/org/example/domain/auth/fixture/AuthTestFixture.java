package org.example.domain.auth.fixture;

import org.example.domain.auth.dto.request.LoginRequest;
import org.example.domain.auth.dto.request.RefreshRequest;
import org.example.domain.auth.dto.response.TokenResponse;
import org.example.domain.auth.entity.AuthEntity;
import org.example.domain.member.MemberTestFixture;

public class AuthTestFixture {
    public static LoginRequest createLoginRequest() {
        return LoginRequest.builder().email("valid@example.com").password("validPassword123").build();
    }

    public static String createAccessToken() {
        return "mockAccessToken";
    }

    public static String createRefreshToken() {
        return "mockRefreshToken";
    }

    public static RefreshRequest createRefreshRequest() {
        return new RefreshRequest(createAccessToken(),createRefreshToken());
    }
    public static TokenResponse createTokenResponse( ){
        return TokenResponse.builder().accessToken(createAccessToken()).refreshToken(createRefreshToken()).build();
    }

    public static AuthEntity createAuthEntity() {
        return AuthEntity.createWith(createRefreshToken());
    }


}
