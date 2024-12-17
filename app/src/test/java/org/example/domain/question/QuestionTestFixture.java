package org.example.domain.question;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.login.LoginTestFixture;
import org.example.domain.login.dto.request.LoginForm;
import org.example.domain.login.dto.response.LoginResponse;
import org.example.domain.member.MemberTestFixture;
import org.example.domain.member.dto.MemberEditForm;
import org.example.domain.member.dto.MemberForm;
import org.example.domain.member.entity.Member;
import org.example.domain.question.dto.request.QuestionCreateForm;
import org.example.domain.question.entity.Question;
import org.junit.jupiter.api.TestTemplate;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

@Slf4j
public class QuestionTestFixture {

    // 회원 가입 후 로그인한 후 session 전달
    public static String joinAndLogin(TestRestTemplate restTemplate, int port) {
        // 회원 가입
        MemberForm validMemberForm = MemberTestFixture.createValidMemberForm();

        ResponseEntity<Member> JoinResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/profile/add",
                validMemberForm,
                Member.class
        );

        // 로그인
        LoginForm validLoginForm = LoginTestFixture.createValidLoginForm();
        ResponseEntity<Member> loginResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/login",
                validLoginForm,
                Member.class
        );

        return loginResponse.getHeaders().getFirst("set-cookie");

    }

    // 질문 생성 폼 데이터 생성 - 검증 조건 충족
    public static QuestionCreateForm createValidQuestionForm() {
        return QuestionCreateForm.builder()
                .question_language("en")
                .title("this is question")
                .content("this is content")
                .point(50)
                .build();
    }

    // 질문 생성 폼 데이터 생성 - 검증 조건 불충족 (지원 하지 않는 언어 입력)
    public static QuestionCreateForm createInvalidQuestionForm() {
        return QuestionCreateForm.builder()
                .question_language("it")
                .title("this is question")
                .content("this is content")
                .point(50)
                .build();
    }

    // 질문 조회, 수정, 삭제를 위한 테스팅 환경 - 회원 가입 후 로그인 한 사용자가 질문 생성
    public static Question createQuestion(TestRestTemplate restTemplate, int port) {
        String session = joinAndLogin(restTemplate, port);

        // sessionId를 요청 헤더에 추가
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, session);

        QuestionCreateForm validQuestionForm = createValidQuestionForm();
        HttpEntity<QuestionCreateForm> request = new HttpEntity<>(validQuestionForm, headers);

        // When
        ResponseEntity<Question> response = restTemplate.exchange(
                "http://localhost:" + port + "/question/create",
                HttpMethod.POST,
                request,
                Question.class
        );

        return response.getBody();
    }

}
