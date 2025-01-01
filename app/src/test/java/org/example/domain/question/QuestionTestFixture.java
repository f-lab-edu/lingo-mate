package org.example.domain.question;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.login.LoginTestFixture;
import org.example.domain.login.dto.request.LoginForm;
import org.example.domain.login.dto.response.LoginResponse;
import org.example.domain.member.MemberTestFixture;
import org.example.domain.member.dto.MemberEditForm;
import org.example.domain.member.dto.MemberForm;
import org.example.domain.member.entity.Member;
import org.example.domain.question.dto.request.CommentForm;
import org.example.domain.question.dto.request.QuestionCreateForm;
import org.example.domain.question.dto.request.QuestionEditForm;
import org.example.domain.question.entity.Question;
import org.junit.jupiter.api.TestTemplate;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.security.PublicKey;
import java.util.List;

import static org.example.domain.login.LoginTestFixture.*;
import static org.example.domain.member.MemberTestFixture.*;

@Slf4j
public class QuestionTestFixture {

    // 회원 가입 후 로그인한 후 session 전달
    public static String joinAndLogin(TestRestTemplate restTemplate, int port) {
        // 회원 가입
        MemberForm validMemberForm = createMemberForm();

        ResponseEntity<Member> JoinResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/profile/add",
                validMemberForm,
                Member.class
        );

        // 로그인
        LoginForm validLoginForm = createValidLoginForm();
        ResponseEntity<Member> loginResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/login",
                validLoginForm,
                Member.class
        );

        return loginResponse.getHeaders().getFirst("set-cookie");

    }

    // 질문 생성 폼
    public static QuestionCreateForm createQuestionForm() {
        return new QuestionCreateForm("en", "this is question", "this is content", 50);
    }

    // 질문 수정 폼
    public static QuestionEditForm questionEditForm() {
        return QuestionEditForm.builder()
                .question_language("kr")
                .title("changed title")
                .build();
    }

    // 검색하는 키워드가 포함된 질문 생성
    public static QuestionCreateForm createQuestionKeyword() {
        return new QuestionCreateForm("en", "this is target", "this is content", 50);
    }


    // 댓글 생성 폼
    public static CommentForm createCommentForm() {
        return new CommentForm("새로운 댓글");
    }

    // 질문이 달린 댓글
    public static CommentForm createEditCommentForm() {
        return new CommentForm("수정된 댓글");
    }
}
