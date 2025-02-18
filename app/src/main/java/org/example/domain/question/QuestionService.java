package org.example.domain.question;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.comment.Comment;
import org.example.domain.comment.CommentRepository;
import org.example.domain.member.MemberService;
import org.example.domain.member.entity.Member;
import org.example.domain.question.dto.request.CommentRequest;
import org.example.domain.question.dto.response.QuestionResponse;
import org.example.domain.question.dto.request.QuestionCreateRequest;
import org.example.domain.question.dto.request.QuestionEditRequest;
import org.example.domain.question.entity.Question;
import org.example.domain.question.exception.CommentNotFound;
import org.example.domain.question.exception.QuestionNotFound;
import org.example.domain.question.exception.UnauthorizedModification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final MemberService memberService;
    private final CommentRepository commentRepository;

    // 질문 생성
    @Async
    public CompletableFuture<QuestionResponse> addQuestion(QuestionCreateRequest request, Long memberId) throws ExecutionException, InterruptedException {
        return CompletableFuture.supplyAsync(() -> {
            Question question = Question.create(request);
            Member member = null;
            try {
                member = memberService.findMember(memberId).get();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
            //Member member = questionRepository.findMemberById(memberId).orElseThrow(MemberNotFound::new);
            member.addQuestion(question);
            return QuestionResponse.create(question);
        });
    }

    // 질문 목록 조회
    @Async
    public CompletableFuture<Page<QuestionResponse>> findAllQuestion(Pageable pageable) {
        return CompletableFuture.supplyAsync(() -> {
            Page<Question> questions = questionRepository.findAll(pageable);
            return questions.map(QuestionResponse::create);
        });
    }

    // 사용자 생성 질문 조회
    @Async
    public CompletableFuture<Page<QuestionResponse>> findQuestionByMemberId(Pageable pageable, Long memberId) {
        return CompletableFuture.supplyAsync(() -> {
            Page<Question> questions = questionRepository.findByMemberId(pageable, memberId);
            return questions.map(QuestionResponse::create);
        });
    }


    // 질문 수정
    @Async
    public CompletableFuture<Question> modifyQuestion(Long memberId, Long questionId, QuestionEditRequest questionEditRequest) {

        return CompletableFuture.supplyAsync(() -> {
            Question question = questionRepository.findById(questionId).orElseThrow(QuestionNotFound::new);
            if(!question.getMember().getId().equals(memberId)) {
                throw new UnauthorizedModification();
            }
            return question.editQuestion(questionEditRequest);
        });
    }


    // 질문 삭제
    @Async
    public CompletableFuture<Void> removeQuestion(Long memberId, Long questionId) {
        return CompletableFuture.runAsync(() -> {
            Question question = questionRepository.findById(questionId).orElseThrow(QuestionNotFound::new);

            if(!question.getMember().getId().equals(memberId)) {
                throw new UnauthorizedModification();
            }
            questionRepository.deleteById(questionId);
        });
    }

    // 질문 댓글 추가
    @Async
    public CompletableFuture<Comment> addComment(Long questionId, Long memberId, CommentRequest commentRequest) {
        return CompletableFuture.supplyAsync(() -> {
            // 댓글 달릴 질문 조회
            Question question = questionRepository.findById(questionId).orElseThrow(QuestionNotFound::new);

            // 댓글 생성
            Comment comment = Comment.createComment(commentRequest);

            // 연관관계 설정
            question.addComment(comment);
            Member member = null;
            try {
                member = memberService.findMember(memberId).get();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
            member.addComment(comment);

            return comment;
        });
    }

    // 질문 댓글 수정
    @Async
    public CompletableFuture<Comment> modifyComment(Long questionId, Long commentId, Long memberId, CommentRequest commentRequest) {
        return CompletableFuture.supplyAsync(() -> {
            // 댓글을 수정할 질문을 조회한다
            Question question = questionRepository.findById(questionId).orElseThrow(QuestionNotFound::new);

            // 수정할 댓글을 찾는다
            Comment target = question.getComments().stream()
                    .filter(comment -> comment.getId().equals(commentId))
                    .findFirst()
                    .orElseThrow(CommentNotFound::new);

            // 댓글을 수정할 권한이 있는 지 확인한다
            if(!target.getMember().getId().equals(memberId)){
                throw new UnauthorizedModification();
            }

            // 댓글을 수정한다
            return target.editComment(commentRequest);
        });
    }


    // 질문 댓글 삭제
    @Async
    public CompletableFuture<Void> removeComment(Long questionId, Long commentId, Long memberId) {
        return CompletableFuture.runAsync(() -> {
            // 댓글을 삭제할 질문을 조회한다
            Question question = questionRepository.findById(questionId).orElseThrow(QuestionNotFound::new);

            // 삭제할 댓글을 찾는다
            Comment target = question.getComments().stream()
                    .filter(comment -> comment.getId().equals(commentId))
                    .findFirst()
                    .orElseThrow(CommentNotFound::new);

            // 댓글을 삭제할 권한이 있는 지 확인한다
            if(!target.getMember().getId().equals(memberId)){
                throw new UnauthorizedModification();
            }

            // 댓글을 삭제한다
            commentRepository.deleteById(commentId);
        });
    }

    // 질문 검색 기능.. (elasticsearch)

}
