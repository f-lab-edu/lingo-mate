package org.example.domain.question.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
public class Question {
    private static Long sequence = 0L;
    private Long id;
    private String author;
    private String question_language;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer point;
    private List<Comment> comments;

    public Question(QuestionCreateForm questionCreateForm) {
        this.id = ++sequence;
        this.author = questionCreateForm.getAuthor();
        this.question_language = questionCreateForm.getQuestion_language();
        this.title = questionCreateForm.getTitle();
        this.content = questionCreateForm.getContent();
        this.point = questionCreateForm.getPoint();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.comments = new ArrayList<>();
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
        if (updatedQuestion.getUpdatedAt() != null) {
            this.updatedAt = updatedQuestion.getUpdatedAt();
        } else {
            this.updatedAt = LocalDateTime.now(); // 수정 시간이 null이면 현재 시간으로 설정
        }
        if (updatedQuestion.getPoint() != null) {
            this.point = updatedQuestion.getPoint();
        }
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
