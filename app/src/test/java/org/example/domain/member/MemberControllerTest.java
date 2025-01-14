package org.example.domain.member;

import org.example.domain.member.dto.request.MemberEditRequest;
import org.example.domain.member.dto.request.MemberJoinRequest;
import org.example.domain.member.dto.response.MemberResponse;
import org.example.domain.member.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Rollback
class MemberControllerTest {
    @Autowired
    MemberController memberController;

    @Autowired
    MemberRepository memberRepository;
    @Test
    void memberAdd() {
        //Given
        MemberJoinRequest MemberJoinRequest = MemberTestFixture.createMemberJoinRequest();

        //When
        ResponseEntity<MemberResponse> response = memberController.memberAdd(MemberJoinRequest);

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getUsername()).isEqualTo("validUsername");
        assertThat(response.getBody().getEmail()).isEqualTo("valid@example.com");
    }

    @Test
    @Transactional
    // Member와 Language가 1:N 연관관계를 맺고 있고 지연 로딩으로 설정되어 있음.
    // 테스트 코드를 호출하는 동안에는 영속성 컨텍스트가 닫혀있으므로 지연 로딩 을 사용할 수 없어 @Transactional 이용
    void memberDetails() {
        //Given
        Member member = MemberTestFixture.createMember();
        Member saveMember = memberRepository.save(member);

        //When
        ResponseEntity<MemberResponse> response = memberController.memberDetails(saveMember.getId());

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getUsername()).isEqualTo("validUsername");
        assertThat(response.getBody().getEmail()).isEqualTo("valid@example.com");
    }


    @Test
    void memberModify() {
        //Given
        Member member = MemberTestFixture.createMember();
        Member saveMember = memberRepository.save(member);

        MemberEditRequest validMemberEditRequest = MemberTestFixture.createValidMemberEditForm();

        //When
        ResponseEntity<MemberResponse> response = memberController.memberModify(member.getId(), validMemberEditRequest);

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getUsername()).isEqualTo("updatedUsername");
        assertThat(response.getBody().getIntroduction()).isEqualTo("This is my updated introduction.");
    }

}