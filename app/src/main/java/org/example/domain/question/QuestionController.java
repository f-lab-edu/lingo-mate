package org.example.domain.question;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.auth.annotation.LoginMember;
import org.example.domain.comment.Comment;
import org.example.domain.comment.CommentResponse;
import org.example.domain.question.dto.response.QuestionResponse;
import org.example.domain.question.dto.request.CommentRequest;
import org.example.domain.question.dto.request.QuestionCreateRequest;
import org.example.domain.question.dto.request.QuestionEditRequest;
import org.example.domain.question.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@RestController
@RequestMapping("/api/question")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    // 질문 생성
    @PostMapping("/create")
    @Async
    public CompletableFuture<ResponseEntity<QuestionResponse>> questionAdd(@LoginMember Long memberId, @Valid @RequestBody QuestionCreateRequest questionCreateRequest) {
        return CompletableFuture.supplyAsync(() -> {
            log.debug("memberId = {}", memberId);
            QuestionResponse questionResponse = null;
            try {
                questionResponse = questionService.addQuestion(questionCreateRequest, memberId).get();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
            return ResponseEntity.ok().body(questionResponse);
        });
    }

    // 질문 목록 조회
    @GetMapping("/all")
    @Async
    public CompletableFuture<ResponseEntity<Page<QuestionResponse>>> questionList(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        return CompletableFuture.supplyAsync(() -> {
            Pageable pageable = PageRequest.of(page, size);
            Page<QuestionResponse> questionResponses = null;
            try {
                questionResponses = questionService.findAllQuestion(pageable).get();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
            return ResponseEntity.ok().body(questionResponses);
        });
    }

    // 사용자 생성 질문 조회
    @GetMapping("/my-questions")
    @Async
    public CompletableFuture<ResponseEntity<Page<QuestionResponse>>> questionDetails(
            @LoginMember Long memberId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        return CompletableFuture.supplyAsync(() -> {
            Pageable pageable = PageRequest.of(page, size);
            Page<QuestionResponse> questionResponses = null;
            try {
                questionResponses = questionService.findQuestionByMemberId(pageable, memberId).get();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
            return ResponseEntity.ok().body(questionResponses);
        });
    }


    // 질문 수정
    @PutMapping("/{question_id}/edit")
    @Async
    public CompletableFuture<ResponseEntity<QuestionResponse>> questionModify(@LoginMember Long memberId, @PathVariable(value = "question_id") Long questionId,
                                                           @RequestBody QuestionEditRequest questionEditRequest) {

        return CompletableFuture.supplyAsync(() -> {
            Question question = null;
            try {
                question = questionService.modifyQuestion(memberId, questionId, questionEditRequest).get();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
            QuestionResponse questionResponse = QuestionResponse.create(question);
            return ResponseEntity.ok(questionResponse);
        });
    }

    // 질문 삭제
    @DeleteMapping("/{question_id}/delete")
    @Async
    public CompletableFuture<ResponseEntity<Void>> questionRemove(@LoginMember Long memberId, @PathVariable(value = "question_id") Long questionId) {
        return CompletableFuture.supplyAsync(() -> {
            questionService.removeQuestion(memberId, questionId);
            return ResponseEntity.ok().build();
        });
    }

    // 질문 댓글 추가
    @PostMapping("/{question_id}/comments")
    @Async
    public CompletableFuture<ResponseEntity<CommentResponse>> commentAdd(@LoginMember Long memberId, @PathVariable(value = "question_id") Long questionId,
                                                      @Valid @RequestBody CommentRequest commentRequest) {

        return CompletableFuture.supplyAsync(() -> {
            Comment comment = null;
            try {
                comment = questionService.addComment(questionId, memberId, commentRequest).get();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
            CommentResponse commentResponse = CommentResponse.create(comment);
            return ResponseEntity.ok(commentResponse);
        });
    }

    // 질문 댓글 수정
    @PutMapping("/{question_id}/comments/{comment_id}")
    @Async
    public CompletableFuture<ResponseEntity<CommentResponse>> commentModify(@PathVariable(value = "question_id") Long questionId,
                                                         @PathVariable(value = "comment_id") Long commentId,
                                                         @LoginMember Long memberId,
                                                         @Valid @RequestBody CommentRequest CommentRequest) {
        return CompletableFuture.supplyAsync(() -> {
            Comment comment = null;
            try {
                comment = questionService.modifyComment(questionId, commentId, memberId, CommentRequest).get();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
            CommentResponse commentResponse = CommentResponse.create(comment);
            return ResponseEntity.ok(commentResponse);
        });

    }

    // 질문 댓글 삭제
    @DeleteMapping("/{question_id}/comments/{comment_id}")
    @Async
    public CompletableFuture<ResponseEntity<Void>> commentRemove(@PathVariable(value = "question_id") Long questionId,
                                              @PathVariable(value = "comment_id") Long commentId,
                                              @LoginMember Long memberId) {
        return CompletableFuture.supplyAsync(() -> {
            questionService.removeComment(questionId, commentId, memberId);
            return ResponseEntity.ok().build();
        });
    }

    // 질문 검색???
    @GetMapping("/search")
    @Async
    public void questionSearch(@RequestParam("keyword") String keyword) {

    }

}
