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
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/question")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    // 질문 생성
    @PostMapping("/create")
    public ResponseEntity<QuestionResponse> questionAdd(@LoginMember Long memberId, @Valid @RequestBody QuestionCreateRequest questionCreateRequest) {
        log.debug("memberId = {}", memberId);
        QuestionResponse questionResponse = questionService.addQuestion(questionCreateRequest, memberId);
        return ResponseEntity.ok().body(questionResponse);
    }

    // 질문 목록 조회
    @GetMapping("/all")
    public ResponseEntity<Page<QuestionResponse>> questionList(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<QuestionResponse> questionResponses = questionService.findAllQuestion(pageable);
        return ResponseEntity.ok().body(questionResponses);
    }

    // 사용자 생성 질문 조회
    @GetMapping("/my-questions")
    public ResponseEntity<Page<QuestionResponse>> questionDetails(
            @LoginMember Long memberId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<QuestionResponse> questionResponses = questionService.findQuestionByMemberId(pageable, memberId);
        return ResponseEntity.ok().body(questionResponses);
    }


    // 질문 수정 - 권환 체크 필요
    @PutMapping("/{question_id}/edit")
    public ResponseEntity<QuestionResponse> questionModify(@LoginMember Long memberId, @PathVariable(value = "question_id") Long questionId,
                               @RequestBody QuestionEditRequest questionEditRequest) {
        Question question = questionService.modifyQuestion(memberId, questionId, questionEditRequest);
        QuestionResponse questionResponse = QuestionResponse.create(question);
        return ResponseEntity.ok(questionResponse);
    }

    // 질문 삭제 - 반환값?
    @DeleteMapping("/{question_id}/delete")
    public ResponseEntity<Void> questionRemove(@LoginMember Long memberId, @PathVariable(value = "question_id") Long questionId) {
        questionService.removeQuestion(memberId,questionId);
        return ResponseEntity.ok().build();
    }

    // 질문 댓글 추가
    @PostMapping("/{question_id}/comments")
    public ResponseEntity<CommentResponse> commentAdd(@LoginMember Long memberId, @PathVariable(value = "question_id") Long questionId,
                           @Valid @RequestBody CommentRequest commentRequest) {
        Comment comment = questionService.addComment(questionId, memberId, commentRequest);
        CommentResponse commentResponse = CommentResponse.create(comment);
        return ResponseEntity.ok(commentResponse);
    }

    // 질문 댓글 수정
    @PutMapping("/{question_id}/comments/{comment_id}")
    public ResponseEntity<CommentResponse> commentModify(@PathVariable(value = "question_id") Long questionId,
                              @PathVariable(value = "comment_id") Long commentId,
                              @LoginMember Long memberId,
                              @Valid @RequestBody CommentRequest CommentRequest) {
        Comment comment = questionService.modifyComment(questionId, commentId, memberId, CommentRequest);
        CommentResponse commentResponse = CommentResponse.create(comment);
        return ResponseEntity.ok(commentResponse);

    }

    // 질문 댓글 삭제
    @DeleteMapping("/{question_id}/comments/{comment_id}")
    public ResponseEntity<Void> commentRemove(@PathVariable(value = "question_id") Long questionId,
                              @PathVariable(value = "comment_id") Long commentId,
                              @LoginMember Long memberId) {
        questionService.removeComment(questionId,commentId,memberId);
        return ResponseEntity.ok().build();
    }

    // 질문 검색???
    @GetMapping("/search")
    public void questionSearch(@RequestParam("keyword") String keyword) {

    }

}
