package org.example.domain.member;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.domain.member.dto.request.MemberEditForm;
import org.example.domain.member.dto.request.MemberJoinForm;
import org.example.domain.member.dto.response.MemberDTO;
import org.example.domain.member.entity.Member;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    x
    @PostMapping("/add")
    public ResponseEntity<MemberDTO> memberAdd(@Valid @RequestBody MemberJoinForm memberForm) {
        Member savedMember = memberService.addMember(memberForm);
        MemberDTO memberDTO = MemberDTO.createMemberDTO(savedMember);
        return ResponseEntity.status(201).body(memberDTO);
    }

    // 사용자 프로필 조회
    @GetMapping("/{user_id}")
    public ResponseEntity<MemberDTO> memberDetails(@PathVariable(value = "user_id") Long userId) {
        Member member = memberService.findMember(userId);
        MemberDTO memberDTO = MemberDTO.createMemberDTO(member);
        return ResponseEntity.ok().body(memberDTO);
    }

    // 사용자 프로필 수정 폼에 초기 데이터 제공
    @GetMapping("/{user_id}/edit")
    public ResponseEntity<MemberDTO> memberModify(@PathVariable(value = "user_id") Long userId) {
        Member member = memberService.findMember(userId);
        MemberDTO memberDTO = MemberDTO.createMemberDTO(member);
        return ResponseEntity.ok().body(memberDTO);
    }
    // 사용자 프로필 수정
    @PutMapping("/{user_id}/edit")
    public ResponseEntity<MemberDTO> memberModify(@PathVariable(value = "user_id") Long user_id, @Valid @RequestBody MemberEditForm memberEditForm) {
        Member updateMember = memberService.modifyMember(user_id, memberEditForm);
        MemberDTO memberDTO = MemberDTO.createMemberDTO(updateMember);
        return ResponseEntity.ok().body(memberDTO);
    }
}
