package org.example.domain.member;

import jakarta.validation.Valid;
import org.example.domain.member.dto.MemberEditForm;
import org.example.domain.member.dto.MemberForm;
import org.example.domain.member.entity.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/profile")
public class MemberController {
    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // 회원가입
    @PostMapping("/add")
    public ResponseEntity<Member> memberAdd(@Valid @RequestBody MemberForm memberForm) {
        Member savedMember = memberService.addMember(memberForm);
        return ResponseEntity.status(201).body(savedMember);
    }

    // 사용자 프로필 조회
    @GetMapping("/{user_id}")
    public ResponseEntity<Member> memberDetails(@PathVariable(value = "user_id") Long user_id) {
        Member member = memberService.findMember(user_id);
        return ResponseEntity.ok().body(member);
    }

    // 사용자 프로필 수정
    @PutMapping("/{user_id}/edit")
    public ResponseEntity<Member> memberModify(@PathVariable(value = "user_id") Long user_id, @Valid @RequestBody MemberEditForm memberEditForm) {
        Member updateMember = memberService.modifyMember(user_id, memberEditForm);
        return ResponseEntity.ok().body(updateMember);
    }
}
