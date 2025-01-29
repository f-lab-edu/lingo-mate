package org.example.domain.question;

import org.assertj.core.api.Assertions;
import org.example.domain.comment.Comment;
import org.example.domain.comment.CommentRepository;
import org.example.domain.member.MemberTestFixture;
import org.example.domain.member.entity.Member;
import org.example.domain.question.dto.request.CommentRequest;
import org.example.domain.question.dto.request.QuestionEditRequest;
import org.example.domain.question.dto.response.QuestionResponse;
import org.example.domain.question.dto.request.QuestionCreateRequest;
import org.example.domain.question.entity.Question;
import org.example.domain.question.fixture.CommentTestFixture;
import org.example.domain.question.fixture.QuestionTestFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuestionServiceTest {
    @InjectMocks
    private QuestionService questionService;

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private CommentRepository commentRepository;

    @Test
    void 질문_생성을_확인한다() {
        //Given
        QuestionCreateRequest questionCreateRequest = QuestionTestFixture.createQuestionCreateRequest();
        Member mockMember = QuestionTestFixture.createMockMember();
        Long memberId = 1L;

        //Mocking
        when(questionRepository.findMemberById(any(Long.class))).thenReturn(Optional.of(mockMember));

        //When
        QuestionResponse questionResponse = questionService.addQuestion(questionCreateRequest, memberId);

        //Then
        assertThat(questionResponse.getQuestionLanguage()).isEqualTo("en");
        assertThat(questionResponse.getTitle()).isEqualTo("what does slay means?");
        assertThat(questionResponse.getContent()).isEqualTo("slay? what mean?");
        assertThat(questionResponse.getPoint()).isEqualTo(50);
    }

    @Test
    void 질문을_10개씩_페이징한다() {
        //Given
        Pageable pageable = PageRequest.of(1, 10);
        List<Question> question20 = QuestionTestFixture.createQuestion20();
        PageImpl<Question> page = new PageImpl<>(question20, pageable, question20.size());

        //Mocking
        when(questionRepository.findAll(any(Pageable.class))).thenReturn(page);

        //When
        Page<QuestionResponse> result = questionService.findAllQuestion(pageable);

        //Then
        assertThat(result.getTotalElements()).isEqualTo(20);
        assertThat(result.getNumber()).isEqualTo(1);
        assertThat(result.getSize()).isEqualTo(10);
        assertThat(result.getTotalPages()).isEqualTo(2);

    }

    @Test
    void 사용자가_자신이_등록한_질문을_10개씩_페이징한다(){
        //Given
        Pageable pageable = PageRequest.of(1, 10);
        List<Question> question20 = QuestionTestFixture.createQuestion20();
        Long memberId = 0L;
        PageImpl<Question> page = new PageImpl<>(question20, pageable, question20.size());

        //Mocking
        when(questionRepository.findByMemberId(any(Pageable.class),any(Long.class))).thenReturn(page);

        //When
        Page<QuestionResponse> result = questionService.findQuestionByMemberId(pageable, memberId);

        //Then
        assertThat(result.getTotalElements()).isEqualTo(20);
        assertThat(result.getNumber()).isEqualTo(1);
        assertThat(result.getSize()).isEqualTo(10);
        assertThat(result.getTotalPages()).isEqualTo(2);
    }

    @Test
    void 사용자_자신이_생성한_질문은_수정할_수_있다() {
        //Given
        Long questionId = 0L;
        Long memberId = 0L;
        QuestionEditRequest questionEditRequest = QuestionTestFixture.createQuestionEditRequest();
        Question question = QuestionTestFixture.createQuestion(); // 생성된 Question의 memberId은 OL로 설정됨.
        //Mocking
        when(questionRepository.findById(any(Long.class))).thenReturn(Optional.of(question));
        //When
        Question modifiedQuestion = questionService.modifyQuestion(memberId, questionId, questionEditRequest);
        //Then
        assertThat(modifiedQuestion.getQuestionLanguage()).isEqualTo("ja");
        assertThat(modifiedQuestion.getTitle()).isEqualTo("ありがとう");
        assertThat(modifiedQuestion.getContent()).isEqualTo("what means?");
        assertThat(modifiedQuestion.getPoint()).isEqualTo(50);
    }

    @Test
    void 사용자_자신이_생성한_질문은_삭제할_수_있다() {
        //Given
        Long questionId = 0L;
        Long memberId = 0L;
        Question question = QuestionTestFixture.createQuestion();
        when(questionRepository.findById(any(Long.class))).thenReturn(Optional.of(question));
        //When
        questionService.removeQuestion(memberId,questionId);
        //Then
        verify(questionRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void 질문에_댓글을_추가한다() {
        //Given
        Long questionId = 0L;
        Long memberId = 1L;
        CommentRequest commentRequest = CommentTestFixture.createCommentRequest();
        Comment comment = CommentTestFixture.createComment();
        Question question = QuestionTestFixture.createQuestion();
        Member member = MemberTestFixture.createMember();

        //연관관계 설정
        question.addComment(comment);
        member.addComment(comment);

        //Mocking
        when(questionRepository.findById(anyLong())).thenReturn(Optional.of(question));
        when(questionRepository.findMemberById(anyLong())).thenReturn(Optional.of(member));

        //When
        Comment addedComment = questionService.addComment(questionId, memberId, commentRequest);

        //Then
        assertThat(addedComment.getComment()).isEqualTo("댓글1");
        assertThat(addedComment.getMember().getId()).isEqualTo(1L);
    }

    @Test
    void 사용자_자신이_작성한_댓글은_수정할_수_있다() {
        //Given
        Long questionId = 0L;
        Long commentId = 0L;
        Long memberId = 1L;
        CommentRequest commentRequest = CommentTestFixture.createEditCommentRequest();
        Comment comment = CommentTestFixture.createComment();
        Question question = QuestionTestFixture.createQuestion();
        Member member = MemberTestFixture.createMember();

        //연관관계 설정
        question.addComment(comment);
        member.addComment(comment);

        //Mocking
        when(questionRepository.findById(anyLong())).thenReturn(Optional.of(question));

        //When
        Comment modifiedComment = questionService.modifyComment(questionId, commentId, memberId, commentRequest);

        //Then
        Assertions.assertThat(modifiedComment.getComment()).isEqualTo("수정 댓글1");
    }

    @Test
    void 사용자_자신이_작성한_댓글은_삭제할_수_있다(){
        //Given
        Long questionId = 0L;
        Long commentId = 0L;
        Long memberId = 1L;
        Comment comment = CommentTestFixture.createComment();
        Question question = QuestionTestFixture.createQuestion();
        Member member = MemberTestFixture.createMember();

        //연관관계 설정
        question.addComment(comment);
        member.addComment(comment);

        //Mocking
        when(questionRepository.findById(anyLong())).thenReturn(Optional.of(question));

        //When
        questionService.removeComment(questionId,commentId,memberId);

        //Then
        verify(commentRepository, times(1)).deleteById(anyLong());


    }
}