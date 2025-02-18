package org.example.domain.member;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.auth.annotation.LoginMember;
import org.example.domain.member.dto.request.MemberEditRequest;
import org.example.domain.member.dto.request.MemberJoinRequest;
import org.example.domain.member.dto.response.MemberResponse;
import org.example.domain.member.entity.Member;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final MemberService memberService;

    // 사용자 프로필 조회, 사용자 기본 정보 제공
    @GetMapping("/{user_id}")
    @Async
    public CompletableFuture<ResponseEntity<MemberResponse>> memberDetails(@PathVariable(value = "user_id") Long userId) {
        return CompletableFuture.supplyAsync(() -> {
            CompletableFuture<Member> member = memberService.findMember(userId);
            MemberResponse memberResponse = null;
            try {
                memberResponse = MemberResponse.createMemberResponse(member.get());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
            return ResponseEntity.ok().body(memberResponse);
        });
    }

    // 사용자 프로필 수정
    @PutMapping("/{user_id}/edit")
    @Async
    public CompletableFuture<ResponseEntity<MemberResponse>> memberModify(@PathVariable(value = "user_id") Long userId, @Valid @RequestBody MemberEditRequest memberEditRequest) {
        return CompletableFuture.supplyAsync(() -> {
            CompletableFuture<Member> updateMember = memberService.modifyMember(userId, memberEditRequest);
            MemberResponse memberResponse = null;
            try {
                memberResponse = MemberResponse.createMemberResponse(updateMember.get());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
            log.debug("{}", memberResponse.getId());
            return ResponseEntity.ok().body(memberResponse);
        });
    }
}
