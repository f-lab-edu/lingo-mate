package org.example.domain.question;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.question.dto.request.CommentEditForm;
import org.example.domain.question.dto.request.QuestionCreateForm;
import org.example.domain.question.dto.request.QuestionEditForm;
import org.example.domain.question.entity.Comment;
import org.example.domain.question.entity.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Service
public class QuestionService {
    private final QuestionRepository questionRepository;
    @Autowired
    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    // 질문 생성
    public Question addQuestion(QuestionCreateForm createForm, String loggedUsername) {
        Question question = questionRepository.save(createForm, loggedUsername);
        return question;
    }

    // 질문 조회
    public Question findQuestion(Long questionId) {
        Question question = questionRepository.findById(questionId);
        return question;
    }

    // 질문 수정
    public Question modifyQuestion(Long questionId, QuestionEditForm updatedQuestion) {
        Question findQuestion = questionRepository.findById(questionId);
        Question question = findQuestion.editQuestion(updatedQuestion);
        return question;
    }

    // 질문 목록 조회
    public List<Question> findAllQuestion() {
        List<Question> questions = questionRepository.findAll();
        return questions;
    }

    // 질문 삭제
    public Question removeQuestion(Long questionId) {
        Question question = questionRepository.deleteById(questionId);
        return question;
    }

    // 키워드 질문 검색
    public List<Question> searchQuestion(@RequestParam("keyword") String keyword) {
        List<Question> searched = questionRepository.findByKeyword(keyword);
        return searched;
    }

    // 질문 댓글 추가
    public Question addComment(Long questionId, Comment comment) {
        Question question = questionRepository.findById(questionId);
        question.addComment(comment);
        return question;
    }

    // 질문 댓글 수정
    public Question modifyComment(Long questionId, Long commentId, CommentEditForm commentEditForm) {
        Question question = questionRepository.findById(questionId);
        question.editComment(commentEditForm, commentId);
        return question;
    }

    // 질문 댓글 삭제
    @DeleteMapping("/{q_id}/comments/{c_id}")
    public Comment removeComment(Long questionId, Long commentId) {
        Question findQuestion = questionRepository.findById(questionId);
        return findQuestion.deleteComment(commentId);
    }
}
