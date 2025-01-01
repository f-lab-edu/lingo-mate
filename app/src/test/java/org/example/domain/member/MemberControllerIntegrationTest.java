package org.example.domain.member;


import lombok.extern.slf4j.Slf4j;
import org.example.domain.member.dto.MemberEditForm;
import org.example.domain.member.dto.MemberForm;
import org.example.domain.member.entity.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.domain.member.MemberTestFixture.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Slf4j
public class MemberControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void 회원가입_성공() {
        // Given
        MemberForm memberForm = createMemberForm();

        // When
        ResponseEntity<Member> response = restTemplate.postForEntity(
                "/profile/add",
                memberForm,
                Member.class
        );

        Member member = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(member).isNotNull();
        assertThat(member.getEmail()).isEqualTo("valid@example.com");
        assertThat(member.getUsername()).isEqualTo("validUsername");
        assertThat(member.getPassword()).isEqualTo("validPassword123");
        assertThat(member.getNationality()).isEqualTo("USA");
        assertThat(member.getNative_lang()).isEqualTo("en");
        assertThat(member.getLearning()).contains("fr", "ja");
        assertThat(member.getIntroduction()).isEqualTo("I am learning languages!");
    }

    @Test
    void 회원가입_실패_정규식_불만족() {

        // Given
        MemberForm memberForm = createMemberForm();
        memberForm.setLearning(List.of("it", "th")); // 배우는 언어는 ko|en|ja|cn|fr|ar|es|ru 중 하나 여야 함.

        // When
        ResponseEntity<Member> response = restTemplate.postForEntity(
                "/profile/add",
                memberForm,
                Member.class
        );

        Member member = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void 프로필_조회_테스트_성공() {

        // Given
        Member save = memberRepository.save(fakeMember());
        Long memberId = save.getId();

        // When
        ResponseEntity<Member> response = restTemplate.getForEntity(
                "/profile/" + memberId,
                Member.class
        );
        log.debug("response: {}", response.getBody());
        Member body = response.getBody();
        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(body).isNotNull();
        assertThat(body.getId()).isEqualTo(1L);
    }

    @Test
    void 존재하지_않는_회원_조회() {

        // Given
        Long nonExistentId = 999999L;

        // When
        ResponseEntity<String> response = restTemplate.getForEntity(
                "/profile/" + nonExistentId,
                String.class
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void 프로필_수정_성공() {

        // Given
        Member save = memberRepository.save(fakeMember());
        Long memberId = save.getId();

        // create validEditForm
        MemberEditForm validEditForm = createValidMemberEditForm();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<MemberEditForm> editRequest = new HttpEntity<>(validEditForm, headers);

        // When
        ResponseEntity<Member> response = restTemplate.exchange(
                "/profile/" + memberId + "/edit",
                HttpMethod.PUT,
                editRequest,
                Member.class
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getUsername()).isEqualTo("updatedUsername");
        assertThat(response.getBody().getNationality()).isEqualTo("CAN");
        assertThat(response.getBody().getNative_lang()).isEqualTo("fr");
        assertThat(response.getBody().getLearning()).contains("es", "cn");
        assertThat(response.getBody().getIntroduction()).isEqualTo("This is my updated introduction.");
    }


    @Test
    @DisplayName("사용자 프로필 수정 테스트 - 회원 조회 실패")
    void editMemberTestNotFoundFail() {

        // Given
        Long nonExistentId = 999999L;

        // create validEditForm
        MemberEditForm validEditForm = createValidMemberEditForm();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<MemberEditForm> editRequest = new HttpEntity<>(validEditForm, headers);

        // When
        ResponseEntity<String> response = restTemplate.exchange(
                "/profile/" + nonExistentId + "/edit",
                HttpMethod.PUT,
                editRequest,
                String.class
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
