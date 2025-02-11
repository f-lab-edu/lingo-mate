package org.example.domain.question;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.domain.comment.Comment;
import org.example.domain.member.MemberTestFixture;
import org.example.domain.member.entity.Member;
import org.example.domain.question.dto.request.CommentRequest;
import org.example.domain.question.dto.request.QuestionEditRequest;
import org.example.domain.question.dto.response.QuestionResponse;
import org.example.domain.question.dto.request.QuestionCreateRequest;
import org.example.domain.question.entity.Question;
import org.example.domain.question.fixture.CommentTestFixture;
import org.example.domain.question.fixture.QuestionTestFixture;
import org.example.helper.MockBeanInjection;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(QuestionController.class)
class QuestionControllerWebMvcTest extends MockBeanInjection {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private final String token = "Bearer Token";

    @Test
    void 질문을_생성한다() throws Exception {
        //Given
        QuestionCreateRequest questionCreateRequest = QuestionTestFixture.createQuestionCreateRequest();
        QuestionResponse questionResponse = QuestionTestFixture.createQuestionResponse();

        //When
        when(authService.isValidAccessToken("Token")).thenReturn(true);
        when(questionService.addQuestion(any(), any())).thenReturn(questionResponse);

        //Then
        mockMvc.perform(post("/api/question/create")
                        .header(AUTHORIZATION, token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(questionCreateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.questionLanguage").value("en"))
                .andExpect(jsonPath("$.title").value("what does slay means?"))
                .andExpect(jsonPath("$.content").value("slay? what mean?"))
                .andExpect(jsonPath("$.point").value(50));
    }

    @Test
    void 질문을_10개씩_조회한다() throws Exception {
        //Given
        Pageable pageRequest = PageRequest.of(0, 10);
        Page<QuestionResponse> page = new PageImpl<>(QuestionTestFixture.createQuestionResponse20(),pageRequest,20);

        //When
        when(authService.isValidAccessToken("Token")).thenReturn(true);
        when(questionService.findAllQuestion(any(Pageable.class))).thenReturn(page);

        //Then
        mockMvc.perform(get("/api/question/all")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(20))
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.size").value(10));
    }

    @Test
    void 사용자_자신이_생성한_질문을_10개씩_조회한다() throws Exception {
        //Given
        PageRequest pageRequest = PageRequest.of(1, 10);
        Page<QuestionResponse> page = new PageImpl<>(QuestionTestFixture.createQuestionResponse20(), pageRequest, 20);

        //When
        when(authService.isValidAccessToken("Token")).thenReturn(true);
        when(questionService.findQuestionByMemberId(any(Pageable.class), any(Long.class))).thenReturn(page);

        //Then
        mockMvc.perform(get("/api/question/my-questions")
                        .param("page", "1")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(20))
                .andExpect(jsonPath("$.number").value(1))
                .andExpect(jsonPath("$.size").value(10));
    }

    @Test
    void 질문을_수정한다() throws Exception {
        //Given
        Question question = QuestionTestFixture.createQuestion();
        QuestionEditRequest questionEditRequest = QuestionTestFixture.createQuestionEditRequest();
        Question editedQuestion = question.editQuestion(questionEditRequest);

        //When
        when(authService.isValidAccessToken("Token")).thenReturn(true);
        when(questionService.modifyQuestion(any(Long.class),any(Long.class), any(QuestionEditRequest.class))).thenReturn(editedQuestion);

        //Then
        mockMvc.perform(put("/api/question/0/edit")
                .header(AUTHORIZATION,token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(questionEditRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.questionLanguage").value("ja"))
                .andExpect(jsonPath("$.title").value("ありがとう"))
                .andExpect(jsonPath("$.content").value("what means?"))
                .andExpect(jsonPath("$.point").value(50));
    }

    @Test
    void 질문을_삭제한다() throws Exception {
        // Given
        Long memberId = 1L;
        Long questionId = 100L;

        // When
        when(authService.isValidAccessToken("Token")).thenReturn(true);
        doNothing().when(questionService).removeQuestion(memberId, questionId);

        // Then
        mockMvc.perform(delete("/api/question/{question_id}/delete",questionId)
                        .header(AUTHORIZATION, token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()); // HTTP 200 상태 코드 확인
    }

    @Test
    void 질문의_댓글을_생성한다() throws Exception {
        //Given
        CommentRequest commentRequest = CommentTestFixture.createCommentRequest();
        Comment comment = CommentTestFixture.createComment();

        //연관관계 설정
        Member member = MemberTestFixture.createMember();
        Question question = QuestionTestFixture.createQuestion();
        member.addComment(comment);
        question.addComment(comment);

        //When
        when(authService.isValidAccessToken("Token")).thenReturn(true);
        when(questionService.addComment(anyLong(), anyLong(), any())).thenReturn(comment);

        //Then
        mockMvc.perform(post("/api/question/0/comments")
                .header(AUTHORIZATION,token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comment").value("댓글1"))
                .andExpect(jsonPath("$.memberId").value(1L))
                .andExpect(jsonPath("$.questionId").value(0L));

    }


    @Test
    void 질문의_댓글을_수정한다() throws Exception {
        //Given
        CommentRequest editCommentRequest = CommentTestFixture.createEditCommentRequest();
        Comment comment = CommentTestFixture.createComment();

        //연관관계 설정
        Member member = MemberTestFixture.createMember();
        Question question = QuestionTestFixture.createQuestion();
        member.addComment(comment);
        question.addComment(comment);

        //수정된 댓글
        Comment editComment = comment.editComment(editCommentRequest);

        //When
        when(authService.isValidAccessToken("Token")).thenReturn(true);
        when(questionService.modifyComment(anyLong(), anyLong(), anyLong(), any())).thenReturn(editComment);

        //Then
        mockMvc.perform(put("/api/question/0/comments/0")
                        .header(AUTHORIZATION,token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(editCommentRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comment").value("수정 댓글1"))
                .andExpect(jsonPath("$.memberId").value(1L))
                .andExpect(jsonPath("$.questionId").value(0L));

    }

    @Test
    void 질문의_댓글을_삭제한다() throws Exception {
        // Given
        Long questionId = 0L;
        Long commentId = 0L;
        Long memberId = 1L;

        // When
        when(authService.isValidAccessToken("Token")).thenReturn(true);
        doNothing().when(questionService).removeComment(questionId,commentId,memberId);

        // Then
        mockMvc.perform(delete("/api/question/{question_id}/comments/{comment_id}", questionId,commentId)
                        .header(AUTHORIZATION, token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()); // HTTP 200 상태 코드 확인

    }
}