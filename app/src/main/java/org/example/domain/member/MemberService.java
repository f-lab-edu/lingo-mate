package org.example.domain.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.language.Language;
import org.example.domain.member.dto.request.MemberEditRequest;
import org.example.domain.member.dto.request.MemberJoinRequest;
import org.example.domain.member.entity.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;

    // 회원가입
    @Transactional
    public Member addMember(MemberJoinRequest MemberJoinRequest){
        Member member = Member.createMember(MemberJoinRequest);

        for(String lang : MemberJoinRequest.getLearning()) {
            Language language = Language.createLanguage(lang);
            member.addLearning(language);
        }

        return memberRepository.save(member);
    }

    // 사용자 프로필 조회
    public Member findMember(Long user_id){

        Member member = memberRepository.findById(user_id).get();

        // 사용자 조회 실패
        if(member == null) {
            throw new RuntimeException("존재하지 않는 사용자입니다.");
        }

        return member;
    }

    // 사용자 프로필 수정
    @Transactional
    public Member modifyMember(Long user_id, MemberEditRequest memberEditRequest){

        Member member = memberRepository.findById(user_id).get();

        if(member == null) {
            throw new RuntimeException("존재하지 않는 사용자입니다.");
        }

        member.clearLearnings();

        for (String lang : memberEditRequest.getLearning()) {
            Language language = Language.createLanguage(lang);
            member.addLearning(language);
        }


        return member.editMember(memberEditRequest);

    }
}
