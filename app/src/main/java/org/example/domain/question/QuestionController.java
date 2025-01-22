package org.example.domain.question;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.auth.annotation.LoginMember;
import org.example.domain.auth.jwt.JWTUtil;
import org.example.domain.member.MemberRepository;
import org.example.domain.member.entity.Member;
import org.example.domain.question.dto.QuestionResponse;
import org.example.domain.question.dto.request.QuestionCreateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/question")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;
    private final JWTUtil jwtUtil;
    private final MemberRepository memberRepository;

    public Member loginCheck(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String username = jwtUtil.getUsername(token);
        return memberRepository.findByUsername(username).get();

    }

    // 질문 생성
    @PostMapping("/create")
    public ResponseEntity<QuestionResponse> questionAdd(@LoginMember String username, @Valid @RequestBody QuestionCreateRequest questionCreateRequest, HttpServletRequest request) {
        log.debug("username = {}", username);
        QuestionResponse response = questionService.addQuestion(questionCreateRequest, username);
        return ResponseEntity.ok().body(response);
    }

    // 사용자 생성 질문 조회
    @GetMapping("/{member_id}")
    public ResponseEntity<List<QuestionResponse>> questionDetails(@PathVariable(value = "member_id") Long memberId) {
        List<QuestionResponse> response = questionService.findQuestion(memberId);
        return ResponseEntity.ok().body(response);
    }

    /*
    // 질문 수정
    @PutMapping("/{question_id}/edit")
    public ResponseEntity<Question> questionModify(@PathVariable(value = "question_Id") Long questionId,
                                                   @RequestBody QuestionEditRequest updatedQuestion,
                                                   HttpServletRequest request) {
        Question modifiedQuestion = questionService.modifyQuestion(questionId, updatedQuestion);
        return ResponseEntity.ok().body(modifiedQuestion);
    }

    // 질문 삭제
    @DeleteMapping("/{question_id}")
    public ResponseEntity<Question> questionRemove(@PathVariable(value = "question_id") Long questionId) {
        Question question = questionService.removeQuestion(questionId);
        return ResponseEntity.ok().body(question);
    }

    // 질문 목록 조회
    @GetMapping("/all")
    public ResponseEntity<List<Question>> questionList() {
        List<Question> allQuestion = questionService.findAllQuestion();
        return ResponseEntity.ok().body(allQuestion);
    }


    // 키워드 질문 검색
    @GetMapping("/search")
    public ResponseEntity<List<Question>> questionSearch(@RequestParam("keyword") String keyword) {
        List<Question> questions = questionService.searchQuestion(keyword);
        return ResponseEntity.ok().body(questions);
    }

    // 질문 댓글 추가
    @PostMapping("/{question_id}/comments")
    public ResponseEntity<Question> commentAdd(@PathVariable(value = "question_id") Long questionId,
                                               @Valid @RequestBody CommentRequest commentRequest, HttpServletRequest request)
    {
        Member member = (Member) request.getSession().getAttribute(SessionConst.LOGIN_MEMBER);
        Question question = questionService.addComment(questionId, commentRequest, member);
        return ResponseEntity.ok().body(question);
    }

    // 질문 댓글 수정
    @PutMapping("/{q_id}/comments/{c_id}")
    public ResponseEntity<Question> commentModify(@PathVariable(value = "q_id") Long questionId,
                                @PathVariable(value = "c_id") Long commentId,
                            @Valid @RequestBody CommentRequest commentEditForm,
                          HttpServletRequest request) {
        log.debug("here");
        Member member = (Member)request.getSession().getAttribute(SessionConst.LOGIN_MEMBER);
        log.debug("member = {}", member);
        Question question = questionService.modifyComment(questionId, commentId, commentEditForm, member);
        return ResponseEntity.ok().body(question);
    }

    // 질문 댓글 삭제
    @DeleteMapping("/{q_id}/comments/{c_id}")
    public ResponseEntity<Comment> commentRemove(@PathVariable(value = "q_id") Long questionId,
                                  @PathVariable(value = "c_id") Long commentId)
    {
        Comment comment = questionService.removeComment(questionId, commentId);
        return ResponseEntity.ok().body(comment);
    }

     */
}
