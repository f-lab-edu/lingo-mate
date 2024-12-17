package org.example.domain.question;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.question.dto.request.QuestionCreateForm;
import org.example.domain.question.entity.Question;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.domain.question.QuestionTestFixture.*;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class QuestionControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/question";
    }

    @Test
    @DisplayName("질문 생성 성공 - 로그인 한 사용자가 질문 생성 검증 조건에 맞게 질문 생성")
    void questionAddSuccess() {
        // given

        // 회원 가입 후 로그인 한후 sessionId를 전달 받는다
        String session = joinAndLogin(restTemplate, port);

        // sessionId를 요청 헤더에 추가
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, session);

        QuestionCreateForm validQuestionForm = createValidQuestionForm();
        HttpEntity<QuestionCreateForm> request = new HttpEntity<>(validQuestionForm, headers);

        // When
        ResponseEntity<Question> response = restTemplate.exchange(
                getBaseUrl() + "/create",
                HttpMethod.POST,
                request,
                Question.class
        );

        Question body = response.getBody();

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(body.getUsername()).isEqualTo("validUsername");
        assertThat(body.getQuestion_language()).isEqualTo("en");
        assertThat(body.getTitle()).isEqualTo("this is question");
        assertThat(body.getContent()).isEqualTo("this is content");
        assertThat(body.getPoint()).isEqualTo(50);
    }

    @Test
    @DisplayName("질문 생성 실패 - 로그인 한 사용자가 질문 생성 검증 조건과 다르게 질문 생성")
    void questionAddFailInvalid() {
        // given

        // 회원 가입 후 로그인 한후 sessionId를 전달 받는다
        String session = joinAndLogin(restTemplate, port);

        // sessionId를 요청 헤더에 추가
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, session);

        QuestionCreateForm invalidQuestionForm = createInvalidQuestionForm();
        HttpEntity<QuestionCreateForm> request = new HttpEntity<>(invalidQuestionForm, headers);

        // When
        ResponseEntity<Question> response = restTemplate.exchange(
                getBaseUrl() + "/create",
                HttpMethod.POST,
                request,
                Question.class
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    }

    @Test
    @DisplayName("질문 생성 실패 - 로그인 하지 않은 사용자가 질문 생성 시도 시 질문 생성 실패")
    void questionAddFailUnauthorized() {

        // Given - 사용자 세션 정보가 없는 request
        HttpHeaders headers = new HttpHeaders();
        QuestionCreateForm validQuestionForm = createValidQuestionForm();
        HttpEntity<QuestionCreateForm> request = new HttpEntity<>(validQuestionForm, headers);

        // When
        ResponseEntity<Question> response = restTemplate.exchange(
                getBaseUrl() + "/create",
                HttpMethod.POST,
                request,
                Question.class
        );

        Question body = response.getBody();

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }


    @Test
    @DisplayName("질문 조회 성공")
    void questionDetailsSuccess() {

        // given 회원 가입 후 로그인 한 사용자가 질문 생성
        Long createdQuestionId = createQuestion(restTemplate, port);

        // when
        ResponseEntity<Question> response = restTemplate.getForEntity(
                getBaseUrl() + "/" + createdQuestionId,
                Question.class
        );

        // then
        Question body = response.getBody();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(body.getUsername()).isEqualTo("validUsername");
        assertThat(body.getQuestion_language()).isEqualTo("en");
        assertThat(body.getTitle()).isEqualTo("this is question");
        assertThat(body.getContent()).isEqualTo("this is content");
        assertThat(body.getPoint()).isEqualTo(50);

    }

    @Test
    @DisplayName("질문 조회 실패 - 존재하지 않는 질문 조회 시도")
    void questionDetailsFail() {

        Long notExistedQuestionId = 999L;

        // when
        ResponseEntity<Question> response = restTemplate.getForEntity(
                getBaseUrl() + "/" + notExistedQuestionId,
                Question.class
        );

        // then
        Question body = response.getBody();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    }

    @Test
    void questionModify() {
    }

    @Test
    void questionRemove() {
    }

    @Test
    void questionSearch() {
    }

    @Test
    void questionList() {
    }

    @Test
    void commentAdd() {
    }

    @Test
    void commentModify() {
    }

    @Test
    void commentRemove() {
    }
}