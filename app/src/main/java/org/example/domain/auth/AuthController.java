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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/join")
    public CompletableFuture<ResponseEntity<MemberResponse>> memberAdd(@Valid @RequestBody MemberJoinRequest memberJoinRequest) throws ExecutionException, InterruptedException {
        return authService.addMember(memberJoinRequest).thenApply(member -> {
            MemberResponse memberResponse = MemberResponse.createMemberResponse(member);
            return ResponseEntity.status(HttpStatus.CREATED).body(memberResponse);
        });
    }

    @PostMapping("/login")
    public CompletableFuture<ResponseEntity<TokenResponse>> login(@RequestBody LoginRequest loginRequest) {
        return authService.issueToken(loginRequest).thenApply(tokenResponse -> {
            return ResponseEntity.ok()
                    .header("Authorization", "Bearer " + tokenResponse.getAccessToken())
                    .body(tokenResponse);
        });

    }

    @PostMapping("/refresh")
    public CompletableFuture<ResponseEntity<TokenResponse>> refresh(@RequestBody RefreshRequest refreshRequest) {
        return authService.reissueRefreshToken(refreshRequest).thenApply(tokenResponse ->  {
            return ResponseEntity.ok()
                    .header("Authorization", "Bearer " + tokenResponse.getAccessToken())
                    .body(tokenResponse);
        });
    }
}
