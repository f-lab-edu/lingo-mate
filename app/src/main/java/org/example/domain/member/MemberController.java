package org.example.domain.member;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.domain.member.dto.request.MemberEditRequest;
import org.example.domain.member.dto.request.MemberJoinRequest;
import org.example.domain.member.dto.response.MemberResponse;
import org.example.domain.member.entity.Member;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/add")
    public ResponseEntity<MemberResponse> memberAdd(@Valid @RequestBody MemberJoinRequest memberJoinRequest) {
        Member savedMember = memberService.addMember(memberJoinRequest);
        MemberResponse memberResponse = MemberResponse.createMemberResponse(savedMember);
        return ResponseEntity.status(201).body(memberResponse);

    }

    // 사용자 프로필 조회, 사용자 기본 정보 제공
    @GetMapping("/{user_id}")
    public ResponseEntity<MemberResponse> memberDetails(@PathVariable(value = "user_id") Long userId) {
        Member member = memberService.findMember(userId);
        MemberResponse memberResponse = MemberResponse.createMemberResponse(member);
        return ResponseEntity.ok().body(memberResponse);
    }

    // 사용자 프로필 수정
    @PutMapping("/{user_id}/edit")
    public ResponseEntity<MemberResponse> memberModify(@PathVariable(value = "user_id") Long userId, @Valid @RequestBody MemberEditRequest memberEditRequest) {
        Member updateMember = memberService.modifyMember(userId, memberEditRequest);
        MemberResponse memberResponse = MemberResponse.createMemberResponse(updateMember);
        return ResponseEntity.ok().body(memberResponse);
    }
}
