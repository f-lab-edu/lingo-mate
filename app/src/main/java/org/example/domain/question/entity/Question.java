package org.example.domain.question.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.domain.comment.Comment;
import org.example.domain.member.entity.Member;
import org.example.domain.question.dto.request.CommentRequest;
import org.example.domain.question.dto.request.QuestionCreateRequest;
import org.example.domain.question.dto.request.QuestionEditRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Question {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Long id;
    private String questionLanguage;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int point;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<Comment>();

    private Question(final QuestionCreateRequest request){
        this.questionLanguage = request.getQuestionLanguage();
        this.title = request.getTitle();
        this.content = request.getContent();
        this.point = request.getPoint();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

    }

    // 연관관계 편의 메서드
    public void addComment(Comment comment) {
        this.comments.add(comment);
        comment.setQuestion(this);
    }

    // 생성 메서드
    public static Question createQuestion(QuestionCreateRequest questionCreateRequest) {
        return new Question(questionCreateRequest);
    }


    public Question editQuestion(QuestionEditRequest updatedQuestion) {
        if (updatedQuestion.getQuestion_language() != null) {
            this.questionLanguage = updatedQuestion.getQuestion_language();
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



    // 질문 댓글 수정
    public void editComment(CommentRequest commentEditForm, Long comment_id) {

    }


    // 질문 삭제
    public Comment deleteComment(Long comment_id){
        Comment mockTest = Comment.builder().comment("mock test").build();
        return mockTest;
    }

}
