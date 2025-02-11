package org.example.domain.auth;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.auth.dto.request.LoginRequest;
import org.example.domain.auth.dto.request.RefreshRequest;
import org.example.domain.auth.dto.response.TokenResponse;
import org.example.domain.auth.entity.AuthEntity;
import org.example.domain.auth.exception.RefreshTokenNotFound;
import org.example.domain.auth.exception.TokenExpired;
import org.example.domain.auth.exception.TokenIdsMismatchException;
import org.example.domain.auth.exception.UserNotFound;
import org.example.domain.auth.jwt.JWTUtil;
import org.example.domain.member.MemberRepository;
import org.example.domain.member.entity.Member;
import org.example.domain.member.entity.Role;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final MemberRepository memberRepository;
    private final AuthRepository authRepository;
    private final JWTUtil jwtUtil;
    private final EntityManager em;
    public TokenResponse issueToken(final LoginRequest loginRequest) {
        String email = loginRequest.getEmail();

        // 이메일로 Member 조회
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("회원가입 되어 있지 않습니다."));
        Long memberId = member.getId();
        String username = member.getUsername();
        log.debug("memberId = {}",memberId);
        log.debug("username = {}",username);


        // AccessToken, RefreshToken 생성
        String accessToken = jwtUtil.createAccessToken(memberId, username, Role.USER);
        String refreshToken = jwtUtil.createRefreshToken(memberId, username, Role.USER);

        // refreshToken DB 저장
        AuthEntity authEntity = AuthEntity.createWith(refreshToken, accessToken);
        authEntity.setMember(member);
        authRepository.save(authEntity);

        // AccessToken, RefreshToken을 담은 LoginResponse를 컨트롤러에 전달
        return TokenResponse.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    }

    public TokenResponse reissueRefreshToken(final RefreshRequest refreshRequest) {

        String accessToken = refreshRequest.getAccessToken();
        String refreshToken = refreshRequest.getRefreshToken();

        // refresh 만료 확인한다
        try{
            jwtUtil.isExpired(refreshToken);
        } catch (ExpiredJwtException e) {
            authRepository.deleteByRefreshToken(refreshToken);
            throw new TokenExpired();
        }

        //  Access Token, Refresh Token의 사용자 ID가 일치하는지 확인
        if (!jwtUtil.getId(accessToken).equals(jwtUtil.getId(refreshToken))) {
            throw new TokenIdsMismatchException();
        }

        // refreshToken으로 AuthEntity를 찾는다
        AuthEntity authEntity = authRepository.findByRefreshToken(refreshToken).orElseThrow(RefreshTokenNotFound::new);

        // AuthEntity로 Member를 찾는다
        Member member = memberRepository.findById(authEntity.getId()).orElseThrow(UserNotFound::new);

        // Member 정보로 새로운 accessToken을 재발급한다.
        String newAccessToken = jwtUtil.createAccessToken(member.getId(), member.getUsername(), member.getRole());

        // refreshToken을 파기한 후 다시 만들고 저정한다.
        authRepository.deleteByAuthId(authEntity.getId());
        String newRefreshToken = jwtUtil.createRefreshToken(member.getId(), member.getUsername(), member.getRole());
        AuthEntity newAuthEntity = AuthEntity.createWith(accessToken, newRefreshToken);
        newAuthEntity.setMember(member);

        authRepository.save(newAuthEntity);

        return TokenResponse.builder().accessToken(newAccessToken).refreshToken(newRefreshToken).build();

    }

    public boolean isValidAccessToken(String accessToken){
        return authRepository.findByAccessToken(accessToken);
    }
}
