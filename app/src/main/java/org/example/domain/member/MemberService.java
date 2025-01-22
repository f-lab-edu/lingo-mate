package org.example.domain.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.language.Language;
import org.example.domain.member.dto.request.MemberEditRequest;
import org.example.domain.member.dto.request.MemberJoinRequest;
import org.example.domain.member.entity.Member;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    // 회원가입
    @Transactional
    public Member addMember(MemberJoinRequest memberJoinRequest){

        // 암호화
        String password = memberJoinRequest.getPassword();
        memberJoinRequest.setPassword(bCryptPasswordEncoder.encode(password));

        Member member = Member.createMember(memberJoinRequest);

        for(String lang : memberJoinRequest.getLearning()) {
            Language language = Language.createLanguage(lang);
            member.addLearning(language);
        }

        return memberRepository.save(member);
    }

    // 사용자 프로필 조회
    public Member findMember(Long userId){
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));

        return member;
    }

    // 사용자 프로필 수정
    @Transactional
    public Member modifyMember(Long userId, MemberEditRequest memberEditRequest){

        Member member = memberRepository.findById(userId).orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));

        member.clearLearnings();

        for (String lang : memberEditRequest.getLearning()) {
            Language language = Language.createLanguage(lang);
            member.addLearning(language);
        }


        return member.editMember(memberEditRequest);

    }
}
