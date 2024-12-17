package org.example.domain.member;


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

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class MemberControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/profile";
    }

    @BeforeEach
    void createTestData() {
        Member.resetSequence();
    }

    @Test
    @DisplayName("회원 가입 검증 조건을 충족한 회원 가입 성공 테스트")
    void joinMemberSuccess() {
        // Given
        MemberForm validMemberForm = MemberTestFixture.createValidMemberForm();


        // When
        ResponseEntity<Member> response = restTemplate.postForEntity(
                getBaseUrl() + "/add",
                validMemberForm,
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
    @DisplayName("회원 가입 검증 조건을 불충족 회원 가입 실패 테스트")
    void joinMemberFail() {

        // Given
        MemberForm invalidMemberForm = MemberTestFixture.createInvalidMemberForm();

        // When
        ResponseEntity<String> response = restTemplate.postForEntity(
                getBaseUrl() + "/add",
                invalidMemberForm,
                String.class
        );

        // Then: Validate the response
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("사용자 프로필 조회 테스트 - 성공")
    void findMemberTestSuccess() {

        // Given
        MemberForm validMemberForm = MemberTestFixture.createValidMemberForm();
        ResponseEntity<Member> createResponse = restTemplate.postForEntity(
                getBaseUrl() + "/add",
                validMemberForm,
                Member.class
        );

        Long memberId = createResponse.getBody().getId();

        // When
        ResponseEntity<Member> response = restTemplate.getForEntity(
                getBaseUrl() + "/" + memberId,
                Member.class
        );

        Member member = response.getBody();
        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
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
    @DisplayName("사용자 프로필 조회 테스트 - 실패")
    void findMemberTestFail() {

        // Given
        Long nonExistentId = 999999L;

        // When
        ResponseEntity<String> response = restTemplate.getForEntity(
                getBaseUrl() + "/" + nonExistentId,
                String.class
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("사용자 프로필 수정 테스트 - 성공")
    void editMemberTestSuccess() {

        // Given
        MemberForm validMemberForm = MemberTestFixture.createValidMemberForm();
        ResponseEntity<Member> createResponse = restTemplate.postForEntity(
                getBaseUrl() + "/add",
                validMemberForm,
                Member.class
        );

        Long memberId = createResponse.getBody().getId();

        // create validEditForm
        MemberEditForm validEditForm = MemberTestFixture.createValidMemberEditForm();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<MemberEditForm> editRequest = new HttpEntity<>(validEditForm, headers);

        // When
        ResponseEntity<Member> response = restTemplate.exchange(
                getBaseUrl() + "/" + memberId + "/edit",
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
    @DisplayName("사용자 프로필 수정 테스트 - 검증 조건 부족으로 인한 실패")
    void editMemberTestInvalidFail() {
        // Given
        MemberForm validMemberForm = MemberTestFixture.createValidMemberForm();
        ResponseEntity<Member> createResponse = restTemplate.postForEntity(
                getBaseUrl() + "/add",
                validMemberForm,
                Member.class
        );

        Long memberId = createResponse.getBody().getId();

        // create invalidEditForm
        MemberEditForm invalidEditForm = MemberTestFixture.createInvalidMemberEditForm();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<MemberEditForm> editRequest = new HttpEntity<>(invalidEditForm, headers);

        // When
        ResponseEntity<String> response = restTemplate.exchange(
                getBaseUrl() + "/" + memberId + "/edit",
                HttpMethod.PUT,
                editRequest,
                String.class
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("사용자 프로필 수정 테스트 - 회원 조회 실패")
    void editMemberTestNotFoundFail() {

        // Given
        Long nonExistentId = 999999L;

        // create validEditForm
        MemberEditForm validEditForm = MemberTestFixture.createValidMemberEditForm();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<MemberEditForm> editRequest = new HttpEntity<>(validEditForm, headers);

        // When
        ResponseEntity<String> response = restTemplate.exchange(
                getBaseUrl() + "/" + nonExistentId + "/edit",
                HttpMethod.PUT,
                editRequest,
                String.class
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
