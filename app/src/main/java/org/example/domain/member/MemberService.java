package org.example.domain.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.language.Language;
import org.example.domain.member.dto.request.MemberEditRequest;
import org.example.domain.member.dto.request.MemberJoinRequest;
import org.example.domain.member.entity.Member;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;

    // 사용자 프로필 조회
    @Async
    public CompletableFuture<Member> findMember(Long userId){

        return CompletableFuture.supplyAsync(() -> {
            Member member = memberRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));

            return member;
        });
    }

    // 사용자 프로필 수정
    @Transactional
    @Async
    public CompletableFuture<Member> modifyMember(Long userId, MemberEditRequest memberEditRequest){
        return CompletableFuture.supplyAsync(() -> {
            Member member = memberRepository.findById(userId).orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));

            member.clearLearnings();

            for (String lang : memberEditRequest.getLearning()) {
                Language language = Language.createLanguage(lang);
                member.addLearning(language);
            }


            return member.editMember(memberEditRequest);
        });


    }
}
