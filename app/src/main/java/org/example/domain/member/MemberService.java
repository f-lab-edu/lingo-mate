package org.example.domain.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.language.Language;
import org.example.domain.member.dto.request.MemberEditForm;
import org.example.domain.member.dto.request.MemberJoinForm;
import org.example.domain.member.dto.response.MemberDTO;
import org.example.domain.member.entity.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PutMapping;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;

    // 회원가입
    @Transactional
    public Member addMember(MemberJoinForm memberForm){
        Member member = Member.createMember(memberForm);

        for(String lang : memberForm.getLearning()) {
            Language language = Language.createLanguage(lang);
            member.addLearning(language);
        }

        memberRepository.save(member); // language 도 함께 저장
        return member;
    }

    // 사용자 프로필 조회
    public Member findMember(Long user_id){

        Member member = memberRepository.findById(user_id);

        // 사용자 조회 실패
        if(member == null) {
            throw new RuntimeException("존재하지 않는 사용자입니다.");
        }

        return member;
    }

    // 사용자 프로필 수정
    @PutMapping("/{user_id}/edit")
    @Transactional
    public Member modifyMember(Long user_id, MemberEditForm memberEditForm){

        Member member = memberRepository.findById(user_id);

        if(member == null) {
            throw new RuntimeException("존재하지 않는 사용자입니다.");
        }
        member.clearLearnings();

        for (String lang : memberEditForm.getLearning()) {
            Language language = Language.createLanguage(lang);
            member.addLearning(language);
        }
        return member.editMember(memberEditForm);

    }
}
