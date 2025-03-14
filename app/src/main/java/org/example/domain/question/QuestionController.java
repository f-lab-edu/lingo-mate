package org.example.domain.question;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.auth.annotation.LoginMember;
import org.example.domain.question.dto.response.CommentResponse;
import org.example.domain.member.MemberService;
import org.example.domain.question.dto.response.QuestionResponse;
import org.example.domain.question.dto.request.CommentRequest;
import org.example.domain.question.dto.request.QuestionCreateRequest;
import org.example.domain.question.dto.request.QuestionEditRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@RestController
@RequestMapping("/api/question")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;
    private final MemberService memberService;

    // 질문 생성
    @PostMapping("/create")
    public CompletableFuture<ResponseEntity<QuestionResponse>> questionAdd(@LoginMember Long memberId, @Valid @RequestBody QuestionCreateRequest questionCreateRequest) throws ExecutionException, InterruptedException {
        return questionService.addQuestion(questionCreateRequest, memberId).thenApply(questionResponse -> {
            return ResponseEntity.ok().body(questionResponse);
        });

    }

    // 질문 목록 조회
    @GetMapping("/all")
    public CompletableFuture<ResponseEntity<Page<QuestionResponse>>> questionList(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return questionService.findAllQuestion(pageable).thenApply(questionResponses -> {
            return ResponseEntity.ok().body(questionResponses);
        });

    }

    // 사용자 생성 질문 조회
    @GetMapping("/my-questions")
    public CompletableFuture<ResponseEntity<Page<QuestionResponse>>> questionDetails(
            @LoginMember Long memberId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return questionService.findQuestionByMemberId(pageable, memberId).thenApply(questionResponses -> {
            return ResponseEntity.ok().body(questionResponses);
        });

    }


    // 질문 수정
    @PutMapping("/{question_id}/edit")
    public CompletableFuture<ResponseEntity<QuestionResponse>> questionModify(@LoginMember Long memberId, @PathVariable(value = "question_id") Long questionId,
                                                           @RequestBody QuestionEditRequest questionEditRequest) {

        return questionService.modifyQuestion(memberId,questionId,questionEditRequest).thenApply(question -> {
            QuestionResponse questionResponse = QuestionResponse.create(question);
            return ResponseEntity.ok(questionResponse);
        });

    }

    // 질문 삭제
    @DeleteMapping("/{question_id}/delete")
    public ResponseEntity<Void> questionRemove(@LoginMember Long memberId, @PathVariable(value = "question_id") Long questionId) {
        questionService.removeQuestion(memberId, questionId);
        return ResponseEntity.ok().build();
    }

    // 질문 댓글 추가
    @PostMapping("/{question_id}/comments")
    public CompletableFuture<ResponseEntity<CommentResponse>> commentAdd(@LoginMember Long memberId, @PathVariable(value = "question_id") Long questionId,
                                                                         @Valid @RequestBody CommentRequest commentRequest) {
        return questionService.addComment(questionId,memberId,commentRequest).thenApply(comment -> {
            CommentResponse commentResponse = CommentResponse.create(comment);
            return ResponseEntity.ok(commentResponse);
        });

    }

    // 질문 댓글 조회
    @GetMapping("/{question_id}/comments")
    public CompletableFuture<ResponseEntity<List<CommentResponse>>> commentList(@PathVariable("question_id") Long questionId) {
        return questionService.getCommentList(questionId).thenApply(comments -> {
            List<CommentResponse> responses = comments.stream().map(CommentResponse::create).toList();
            return ResponseEntity.ok(responses);
        });
    }

    // 질문 댓글 수정
    @PutMapping("/{question_id}/comments/{comment_id}")
    public CompletableFuture<ResponseEntity<CommentResponse>> commentModify(@PathVariable(value = "question_id") Long questionId,
                                                         @PathVariable(value = "comment_id") Long commentId,
                                                         @LoginMember Long memberId,
                                                         @Valid @RequestBody CommentRequest commentRequest) {

        return questionService.modifyComment(questionId,commentId,memberId,commentRequest).thenApply(comment -> {
            CommentResponse commentResponse = CommentResponse.create(comment);
            return ResponseEntity.ok(commentResponse);
        });

    }

    // 질문 댓글 삭제
    @DeleteMapping("/{question_id}/comments/{comment_id}")
    public ResponseEntity<Void> commentRemove(@PathVariable(value = "question_id") Long questionId,
                                              @PathVariable(value = "comment_id") Long commentId,
                                              @LoginMember Long memberId) {
        questionService.removeComment(questionId, commentId, memberId);
        return ResponseEntity.ok().build();
    }

    // 질문 검색???
    @GetMapping("/search")
    public void questionSearch(@RequestParam("keyword") String keyword) {

    }

}
