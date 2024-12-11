package org.example.domain.question;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.question.dto.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/question")
public class QuestionController {

    private final QuestionRepository questionRepository;

    // 질문 생성
    @PostMapping("/create")
    public String create(@Valid @ModelAttribute QuestionCreateForm createForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "not ok";
        }
        Question newQuestion = questionRepository.save(createForm);
        log.info("newQuestion = {}", newQuestion);
        return "ok";
    }

    // 질문 조회
    @GetMapping("/{q_id}")
    public Question findQuestion(@PathVariable(value = "q_id") String q_id) {
        return questionRepository.findById(Long.parseLong(q_id));
    }

    // 질문 수정
    @PutMapping("/{q_id}/edit")
    public Question editQuestion(@PathVariable(value = "q_id") String q_id,
                                 @ModelAttribute QuestionEditForm updatedQuestion) {
        Question findQuestion = questionRepository.findById(Long.parseLong(q_id));
        Question editedQuestion = findQuestion.editQuestion(updatedQuestion);
        log.info("editedQuestion={}", editedQuestion);
        return editedQuestion;
    }

    // 질문 목록 조회
    @GetMapping("/all")
    public List<Question> questionList() {
        return questionRepository.findAll();
    }

    // 질문 삭제
    @DeleteMapping("/{q_id}")
    public Question deleteQuestion(@PathVariable(value = "q_id") String q_id) {
        return questionRepository.deleteById(Long.parseLong(q_id));
    }

    // 키워드 질문 검색
    @GetMapping("/search")
    public List<Question> searchQuestion(@RequestParam("keyword") String keyword) {
        List<Question> searched = questionRepository.findByKeyword(keyword);
        return searched;
    }

    // 질문 댓글 추가
    @PostMapping("/{q_id}/comments")
    public Question addComment(@PathVariable(value = "q_id") Long question_id,
                               @Valid @ModelAttribute Comment comment,
                               BindingResult result) {

        Question question = questionRepository.findById(question_id);
        question.addComment(comment);
        return question;
    }

    // 질문 댓글 수정
    @PutMapping("/{q_id}/comments/{c_id}")
    public Question editComment(@PathVariable(value = "q_id") Long question_id,
                                @PathVariable(value = "c_id") Long comment_id,
                            @Valid @ModelAttribute CommentEditForm commentEditForm,
                            BindingResult result) {
        Question question = questionRepository.findById(question_id);
        question.editComment(commentEditForm, comment_id);
        return question;
    }

    // 질문 댓글 삭제
    @DeleteMapping("/{q_id}/comments/{c_id}")
    public Comment deleteComment(@PathVariable(value = "q_id") Long question_id,
                                  @PathVariable(value = "c_id") Long comment_id) {
        Question findQuestion = questionRepository.findById(question_id);
        return findQuestion.deleteComment(comment_id);
    }

}
