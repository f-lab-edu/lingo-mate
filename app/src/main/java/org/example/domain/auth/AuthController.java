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
    private final MemberService memberService;
    @PostMapping("/join")
    @Async
    public CompletableFuture<ResponseEntity<MemberResponse>> memberAdd(@Valid @RequestBody MemberJoinRequest memberJoinRequest) {
        System.out.println("controller: " + Thread.currentThread().getName());
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("controller: " + Thread.currentThread().getName());
            CompletableFuture<Member> savedMember = memberService.addMember(memberJoinRequest);

            try {
                MemberResponse memberResponse = MemberResponse.createMemberResponse(savedMember.get());
                return ResponseEntity.status(HttpStatus.CREATED).body(memberResponse);
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });

    }

    @PostMapping("/login")
    @Async
    public CompletableFuture<ResponseEntity<TokenResponse>> login(@RequestBody LoginRequest loginRequest) {
        return CompletableFuture.supplyAsync(() -> {
            CompletableFuture<TokenResponse> tokenResponse = authService.issueToken(loginRequest);
            try {
                return ResponseEntity.ok()
                        .header("Authorization","Bearer " + tokenResponse.get().getAccessToken())
                        .body(tokenResponse.get());
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @PostMapping("/refresh")
    @Async
    public CompletableFuture<ResponseEntity<TokenResponse>> refresh(@RequestBody RefreshRequest refreshRequest) {
        return CompletableFuture.supplyAsync(() -> {
            CompletableFuture<TokenResponse> tokenResponse = authService.reissueRefreshToken(refreshRequest);
            try {
                return ResponseEntity.ok().header("Authorization", "Bearer " + tokenResponse.get().getAccessToken())
                        .body(tokenResponse.get());
            } catch (InterruptedException| ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
