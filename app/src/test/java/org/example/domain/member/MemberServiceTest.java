package org.example.domain.member;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.language.Language;
import org.example.domain.member.dto.request.MemberEditRequest;
import org.example.domain.member.dto.request.MemberJoinRequest;
import org.example.domain.member.entity.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
class MemberServiceTest {
    @InjectMocks
    private MemberService memberService;
    @Mock
    private MemberRepository memberRepository;

    @Test
    void userId로_사용자를_조회한다() {
        //Given
        MemberJoinRequest memberJoinRequest = MemberTestFixture.createMemberJoinRequest();
        Member mockMember = MemberTestFixture.createMember();

        //Mocking
        when(memberRepository.findById(any(Long.class))).thenReturn(Optional.of(mockMember));

        //When
        Member findMember = memberService.findMember(mockMember.getId());

        //Then
        assertThat(findMember.getId()).isEqualTo(mockMember.getId());
    }

    @Test
    void userId와_MemberEditRequest로_사용자_프로필을_수정한다() {
        //Given
        MemberEditRequest memberEditRequest = MemberTestFixture.createMemberEditRequest();
        Member mockMember = MemberTestFixture.createMember();

        //Mocking
        when(memberRepository.findById(any(Long.class))).thenReturn(Optional.of(mockMember));

        //When
        Member editMember = memberService.modifyMember(mockMember.getId(), memberEditRequest);

        //Then
        assertThat(editMember).isEqualTo(editMember);

        // 배우는 언어가 "fr", "ja"에서 "es", "cn"으로 바뀌는지 확인
        assertThat(editMember.getLearnings())
                .extracting(Language::getLanguage) // Language 객체에서 이름 추출
                .containsExactlyInAnyOrder("es", "cn");
    }
}