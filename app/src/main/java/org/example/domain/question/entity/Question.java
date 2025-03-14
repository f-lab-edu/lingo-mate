package org.example.domain.question.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.domain.comment.Comment;
import org.example.domain.member.entity.Member;
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
    // 생성 메서드
    public static Question create(QuestionCreateRequest questionCreateRequest) {
        return new Question(questionCreateRequest);
    }

    // 연관관계 편의 메서드
    public void addComment(Comment comment) {
        this.comments.add(comment);
        comment.setQuestion(this);
    }


    public Question editQuestion(QuestionEditRequest questionEditRequest) {
        if (questionEditRequest.getQuestionLanguage() != null) {
            this.questionLanguage = questionEditRequest.getQuestionLanguage();
        }
        if (questionEditRequest.getTitle() != null) {
            this.title = questionEditRequest.getTitle();
        }
        if (questionEditRequest.getContent() != null) {
            this.content = questionEditRequest.getContent();
        }
        if (questionEditRequest.getPoint() != null) {
            this.point = questionEditRequest.getPoint();
        }

        this.updatedAt = LocalDateTime.now();

        return this;
    }

}
