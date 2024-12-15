package org.example.domain.member;

import jakarta.validation.Valid;
import org.example.domain.common.ApiResponse;
import org.example.domain.member.dto.MemberEditForm;
import org.example.domain.member.dto.MemberForm;
import org.example.domain.member.entity.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // 회원가입
    public Member save(MemberForm memberForm){
        return memberRepository.save(Member.createMember(memberForm));

    }

    // 사용자 프로필 조회
    public Member getMember(Long user_id){
        return memberRepository.findById(user_id);
    }

    // 사용자 프로필 수정
    @PutMapping("/{user_id}/edit")
    public Member updateMember(Long user_id, MemberEditForm memberEditForm){
        Member member = memberRepository.findById(user_id);
        member.editMember(memberEditForm);
        return member;
    }
}
