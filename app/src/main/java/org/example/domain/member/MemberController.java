package org.example.domain.member;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.domain.member.dto.Member;
import org.example.domain.member.dto.MemberEditForm;
import org.example.domain.member.dto.MemberForm;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profile")
public class MemberController {
    private final MemberRepository memberRepository;

    // 회원가입
    @PostMapping("/add")
    public String save(@Valid @ModelAttribute MemberForm memberForm, BindingResult result){
        if(result.hasErrors()) {
            return "error";
        }
        Member member = new Member(memberForm);
        memberRepository.save(member);
        return "ok";
    }

    // 사용자 프로필 조회
    @GetMapping("/{user_id}")
    public Member findMember(@PathVariable(value = "user_id") Long user_id){
        return memberRepository.findById(user_id);
    }

    // 사용자 프로필 수정
    @PutMapping("/{user_id}/edit")
    public Member editMember(@PathVariable(value = "user_id") Long user_id, @Valid @ModelAttribute MemberEditForm memberEditForm){
        Member member = memberRepository.findById(user_id);
        return member.editMember(memberEditForm);
    }
}
