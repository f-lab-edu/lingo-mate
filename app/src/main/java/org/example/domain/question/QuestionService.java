package org.example.domain.question;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.member.entity.Member;
import org.example.domain.question.dto.request.CommentForm;
import org.example.domain.question.dto.request.QuestionCreateForm;
import org.example.domain.question.dto.request.QuestionEditForm;
import org.example.domain.question.entity.Comment;
import org.example.domain.question.entity.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.example.domain.question.entity.Comment.*;

@Slf4j
@Service
public class QuestionService {
    private final QuestionRepository questionRepository;
    @Autowired
    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    private void isNullException(Question question) {
        if(question == null) {
            log.debug("hit the exception");
            throw new RuntimeException("존재하지 않는 questionId");
        }
    }

    // 질문 생성
    public Question addQuestion(QuestionCreateForm createForm, String loggedUsername) {
        Question question = questionRepository.save(createForm, loggedUsername);
        return question;
    }

    // 질문 조회
    public Question findQuestion(Long questionId) {

        Question question = questionRepository.findById(questionId);
        isNullException(question);

        return question;
    }

    // 질문 수정
    public Question modifyQuestion(Long questionId, QuestionEditForm updatedQuestion) {

        Question findQuestion = questionRepository.findById(questionId);
        isNullException(findQuestion);
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
        isNullException(question);

        return question;
    }

    // 키워드 질문 검색
    public List<Question> searchQuestion(@RequestParam("keyword") String keyword) {
        List<Question> searched = questionRepository.findByKeyword(keyword);
        return searched;
    }

    // 질문 댓글 추가
    public Question addComment(Long questionId, CommentForm commentForm, Member member) {
        // 댓글 달릴 질문 조회
        Question question = questionRepository.findById(questionId);
        isNullException(question);

        // 댓글 생성
        Comment comment = createComment(commentForm, member);
        question.addComment(comment);

        return question;
    }

    // 질문 댓글 수정
    public Question modifyComment(Long questionId, Long commentId, CommentForm commentEditForm, Member member) {

        Question question = questionRepository.findById(questionId);
        isNullException(question);

        question.editComment(commentEditForm, commentId);
        return question;
    }

    // 질문 댓글 삭제
    public Comment removeComment(Long questionId, Long commentId) {

        Question findQuestion = questionRepository.findById(questionId);
        isNullException(findQuestion);

        return findQuestion.deleteComment(commentId);
    }
}
