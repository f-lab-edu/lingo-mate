package org.example.domain.member;

import org.example.domain.language.Language;
import org.example.domain.member.dto.request.MemberJoinRequest;
import org.example.domain.member.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback
class MemberServiceTest {
    @Autowired
    MemberService memberService;
    @Test
    void addMember() {
        //Given
        MemberJoinRequest MemberJoinRequest = MemberTestFixture.createMemberJoinRequest();

        //When
        Member member = memberService.addMember(MemberJoinRequest);

        //Then
        assertThat(member.getEmail()).isEqualTo(MemberJoinRequest.getEmail());
        assertThat(member.getPassword()).isEqualTo(MemberJoinRequest.getPassword());
    }

    @Test
    void findMember() {
        //Given
        MemberJoinRequest MemberJoinRequest = MemberTestFixture.createMemberJoinRequest();
        Member member = memberService.addMember(MemberJoinRequest);

        //When
        Member findMember = memberService.findMember(member.getId());

        //Then
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    void modifyMember() {
        //Given
        MemberJoinRequest MemberJoinRequest = MemberTestFixture.createMemberJoinRequest();
        Member member = memberService.addMember(MemberJoinRequest);

        //When
        Member editMember = memberService.modifyMember(member.getId(), MemberTestFixture.createValidMemberEditForm());

        //Then
        assertThat(editMember).isEqualTo(member);

        // 배우는 언어가 "fr", "ja"에서 "es", "cn"으로 바뀌는지 확인
        assertThat(editMember.getLearnings())
                .extracting(Language::getLanguage) // Language 객체에서 이름 추출
                .containsExactlyInAnyOrder("es", "cn");
    }
}