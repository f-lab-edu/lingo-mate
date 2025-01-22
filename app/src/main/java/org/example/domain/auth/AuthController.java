package org.example.domain.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.auth.dto.request.LoginRequest;
import org.example.domain.auth.dto.request.RefreshRequest;
import org.example.domain.auth.dto.response.TokenResponse;
import org.example.domain.member.MemberService;
import org.example.domain.member.dto.request.MemberJoinRequest;
import org.example.domain.member.dto.response.MemberResponse;
import org.example.domain.member.entity.Member;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final MemberService memberService;
    @PostMapping("/join")
    public ResponseEntity<MemberResponse> memberAdd(@Valid @RequestBody MemberJoinRequest memberJoinRequest) {
        log.debug("join");
        Member savedMember = memberService.addMember(memberJoinRequest);
        MemberResponse memberResponse = MemberResponse.createMemberResponse(savedMember);
        return ResponseEntity.status(201).body(memberResponse);

    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest loginRequest) {
        TokenResponse tokenResponse = authService.issueToken(loginRequest);
        return ResponseEntity.ok()
                .header("Authorization","Bearer " + tokenResponse.getAccessToken())
                .body(tokenResponse);

    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@RequestBody RefreshRequest refreshRequest) {
        TokenResponse tokenResponse = authService.reissueRefreshToken(refreshRequest);
        return ResponseEntity.ok().header("Authorization", "Bearer " + tokenResponse.getAccessToken())
                .body(tokenResponse);
    }
}
