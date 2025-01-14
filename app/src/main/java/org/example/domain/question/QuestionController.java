package org.example.domain.question;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.member.entity.Member;
import org.example.domain.question.dto.request.CommentForm;
import org.example.domain.question.dto.request.QuestionCreateForm;
import org.example.domain.question.dto.request.QuestionEditForm;
import org.example.domain.comment.Comment;
import org.example.domain.question.entity.Question;
import org.example.domain.session.SessionConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/question")
public class QuestionController {

    private final QuestionService questionService;

    @Autowired
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    // 질문 생성
    @PostMapping("/create")
    public ResponseEntity<Question> questionAdd(@Valid @RequestBody QuestionCreateForm createForm, HttpServletRequest request) {
        Member member = (Member)request.getSession().getAttribute(SessionConst.LOGIN_MEMBER);
        Question newQuestion = questionService.addQuestion(createForm, member.getUsername());
        return ResponseEntity.ok().body(newQuestion);
    }

    // 질문 조회
    @GetMapping("/{q_id}")
    public ResponseEntity<Question> questionDetails(@PathVariable(value = "q_id") Long questionId) {
        Question question = questionService.findQuestion(questionId);
        return ResponseEntity.ok().body(question);
    }

    // 질문 수정
    @PutMapping("/{q_id}/edit")
    public ResponseEntity<Question> questionModify(@PathVariable(value = "q_id") Long questionId,
                                                   @RequestBody QuestionEditForm updatedQuestion) {
        Question modifiedQuestion = questionService.modifyQuestion(questionId, updatedQuestion);
        return ResponseEntity.ok().body(modifiedQuestion);
    }

    // 질문 삭제
    @DeleteMapping("/{q_id}")
    public ResponseEntity<Question> questionRemove(@PathVariable(value = "q_id") Long questionId) {
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
    @PostMapping("/{q_id}/comments")
    public ResponseEntity<Question> commentAdd(@PathVariable(value = "q_id") Long questionId,
                               @Valid @RequestBody CommentForm commentForm, HttpServletRequest request)
    {
        Member member = (Member) request.getSession().getAttribute(SessionConst.LOGIN_MEMBER);
        Question question = questionService.addComment(questionId, commentForm, member);
        return ResponseEntity.ok().body(question);
    }

    // 질문 댓글 수정
    @PutMapping("/{q_id}/comments/{c_id}")
    public ResponseEntity<Question> commentModify(@PathVariable(value = "q_id") Long questionId,
                                @PathVariable(value = "c_id") Long commentId,
                            @Valid @RequestBody CommentForm commentEditForm,
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

}
