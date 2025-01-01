package org.example.domain.question;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.member.entity.Member;
import org.example.domain.question.dto.request.CommentForm;
import org.example.domain.question.dto.request.QuestionCreateForm;
import org.example.domain.question.dto.request.QuestionEditForm;
import org.example.domain.question.entity.Comment;
import org.example.domain.question.entity.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.domain.member.MemberTestFixture.fakeMember;
import static org.example.domain.question.QuestionTestFixture.*;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class QuestionControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private QuestionRepository questionRepository;

    @BeforeEach
    void setup() {
        questionRepository.cleanStore();
    }

    @Test
    @DisplayName("질문 생성 성공")
    void questionAddSuccess() {
        // given

        // 회원 가입 후 로그인 한후 sessionId를 전달 받는다
        String session = joinAndLogin(restTemplate, port);

        // sessionId를 요청 헤더에 추가
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, session);

        QuestionCreateForm validQuestionForm = createQuestionForm();
        HttpEntity<QuestionCreateForm> request = new HttpEntity<>(validQuestionForm, headers);

        // When
        ResponseEntity<Question> response = restTemplate.exchange(
                "/question/create",
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
    void 질문_조회_성공() {
        //Given
        Question question = questionRepository.save(createQuestionForm(), "validUsername");
        Long questionId = question.getId();
        //When
        ResponseEntity<Question> response = restTemplate.getForEntity("/question/" + questionId, Question.class);
        Question body = response.getBody();
        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(body.getUsername()).isEqualTo("validUsername");
        assertThat(body.getQuestion_language()).isEqualTo("en");
        assertThat(body.getTitle()).isEqualTo("this is question");
        assertThat(body.getContent()).isEqualTo("this is content");
        assertThat(body.getPoint()).isEqualTo(50);
    }

    @Test
    void 질문_수정_성공() {
        //Given
        Question question = questionRepository.save(createQuestionForm(), "validUsername");
        Long questionId = question.getId();
        QuestionEditForm questionEditForm = questionEditForm();

        //When
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<QuestionEditForm> requestEntity = new HttpEntity<>(questionEditForm, headers);
        ResponseEntity<Question> response = restTemplate.exchange("/question/" + questionId + "/edit", HttpMethod.PUT, requestEntity, Question.class);
        Question body = response.getBody();

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(body.getId()).isEqualTo(questionId);
        assertThat(body.getQuestion_language()).isEqualTo("kr");
        assertThat(body.getTitle()).isEqualTo("changed title");
    }

    @Test
    void 질문_삭제_성공() {
        //Given - 질문 3개 저장 후 1개 삭제
        Question question = questionRepository.save(createQuestionForm(), "validUsername");
        questionRepository.save(createQuestionForm(), "the other user");
        questionRepository.save(createQuestionForm(), "another user");
        Long questionId = question.getId();
        //When
        restTemplate.delete("/question/{q_id}", questionId);
        //Then
        assertThat(questionRepository.findById(questionId)).isNull();
        assertThat(questionRepository.findAll().size()).isEqualTo(2);
    }
    @Test
    void 질문_목록_조회_성공() {
        //Given - 질문 3개 저장
        Question question = questionRepository.save(createQuestionForm(), "validUsername");
        questionRepository.save(createQuestionForm(), "the other user");
        questionRepository.save(createQuestionForm(), "another user");

        //When
        ResponseEntity<List> response = restTemplate.getForEntity("/question/all", List.class);
        List body = response.getBody();

        //Then
        assertThat(body.size()).isEqualTo(3);
    }

    @Test
    void 질문_키워드_검색_성공() {
        // Given - 제목에 "target"이 포함된 질문 2개와 나머지 질문 2개 저장
        questionRepository.save(createQuestionKeyword(), "user1");
        questionRepository.save(createQuestionKeyword(), "user2");
        questionRepository.save(createQuestionForm(), "user3");
        questionRepository.save(createQuestionForm(), "user4");

        // When
        ResponseEntity<List> response = restTemplate.getForEntity("/question/search?keyword={keyword}", List.class, "target");

        // then
        assertThat(response.getBody().size()).isEqualTo(2);
    }


    @Test
    void 댓글_추가_성공() {
        // Given

        // 회원 가입 후 로그인 한후 sessionId를 전달 받는다
        String session = joinAndLogin(restTemplate, port);

        // sessionId를 요청 헤더에 추가
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, session);

        // 댓글 폼 생성
        CommentForm commentForm = createCommentForm();

        // 댓글 폼과 세션 정보가 포함된 헤더로 request 생성
        HttpEntity<CommentForm> request = new HttpEntity<>(commentForm, headers);

        // 댓글이 달릴 질문을 미리 저장
        Question question = questionRepository.save(createQuestionForm(), "validUsername");
        Long questionId = question.getId();


        // When
        ResponseEntity<Question> response = restTemplate.exchange("/question/" + questionId + "/comments", HttpMethod.POST, request, Question.class);
        Question body = response.getBody();
        log.debug(body+"");

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(body.getComments().size()).isEqualTo(1);
        assertThat(body.getComments().get(0).getId()).isEqualTo(1L);
        assertThat(body.getComments().get(0).getMemberId()).isEqualTo(1L);
        assertThat(body.getComments().get(0).getUsername()).isEqualTo("validUsername");

    }
    /*
    - 회원가입 하고 로그인한 사용자가 필요
    - 댓글이 달려있는 질문
    - 댓글의 member_id와 현재 로그인된 사용자의 member_id가 다르면 댓글을 수정하거나 삭제할 수 없음
    - 질문에 comments는 List 형태. Comment.memberId == Member.id
     */
    @Test
    void 댓글_수정_성공() {
        // Given

        // 회원 가입 후 로그인 한후 sessionId를 전달 받는다
        String session = joinAndLogin(restTemplate, port);
        Member member = fakeMember();

        // sessionId를 요청 헤더에 추가
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, session);

        // 질문에 댓글 달기
        Comment comment = Comment.createComment(createCommentForm(), member);
        Question question = questionRepository.save(createQuestionForm(), member.getUsername());
        question.addComment(comment);

        // sessionId를 요청 헤더에 추가
        HttpHeaders headers1 = new HttpHeaders();
        headers1.add(HttpHeaders.COOKIE, session);

        // 댓글 폼 생성
        CommentForm editCommentForm = createEditCommentForm();

        // 댓글 폼과 세션 정보가 포함된 헤더로 request 생성
        HttpEntity<CommentForm> request = new HttpEntity<>(editCommentForm, headers1);

        log.debug("{}, {}",question.getId(), comment.getId());

        // When
        ResponseEntity<Question> response = restTemplate.exchange(
                "/question/" + question.getId() + "/comments/" + comment.getId()
                ,HttpMethod.PUT, request,
                Question.class);
        Question body = response.getBody();
        log.debug("body = {}", body);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void 댓글_삭제_성공() {
    }
}