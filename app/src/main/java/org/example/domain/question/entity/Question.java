package org.example.domain.question.entity;

import lombok.*;
import org.example.domain.member.MemberRepository;
import org.example.domain.question.dto.request.CommentEditForm;
import org.example.domain.question.dto.request.QuestionCreateForm;
import org.example.domain.question.dto.request.QuestionEditForm;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Question {

    private static final AtomicLong sequence = new AtomicLong();

    private Long id;
    private String username;
    private String question_language;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer point;
    private List<Comment> comments;

    // ID 초기화 메서드
    public static void resetSequence() {
        sequence.set(0);
    }

    public static Question createQuestion(QuestionCreateForm questionCreateForm, String loggedUsername) {

        return Question.builder()
                .id(sequence.incrementAndGet()) // ID 자동 증가
                .username(loggedUsername)
                .question_language(questionCreateForm.getQuestion_language())
                .title(questionCreateForm.getTitle())
                .content(questionCreateForm.getContent())
                .point(questionCreateForm.getPoint())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .comments(new ArrayList<>()) // 빈 댓글 리스트 초기화
                .build();
    }


    public Question editQuestion(QuestionEditForm updatedQuestion) {
        if (updatedQuestion.getQuestion_language() != null) {
            this.question_language = updatedQuestion.getQuestion_language();
        }
        if (updatedQuestion.getTitle() != null) {
            this.title = updatedQuestion.getTitle();
        }
        if (updatedQuestion.getContent() != null) {
            this.content = updatedQuestion.getContent();
        }
        if (updatedQuestion.getPoint() != null) {
            this.point = updatedQuestion.getPoint();
        }

        this.updatedAt = LocalDateTime.now();

        return this;
    }

    // 질문 댓글 추가
    public void addComment(Comment createdComment) {
        comments.add(createdComment);
    }

    // 질문 댓글 수정
    public void editComment(CommentEditForm commentEditForm, Long comment_id) {
        Optional<Comment> comment = comments.stream().filter(c -> c.getId().equals(comment_id)).findFirst();
        Comment updatedComment = comment.get().editComment(commentEditForm);
        comments.set(comments.indexOf(comment.get()), updatedComment);
    }

    // 질문 삭제
    public Comment deleteComment(Long comment_id){
        Optional<Comment> comment = comments.stream().filter(c -> c.getId().equals(comment_id)).findFirst();
        boolean result = false;
        return comments.remove(comments.indexOf(comment.get()));
    }

}
