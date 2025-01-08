package org.example.domain.question.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.domain.comment.Comment;
import org.example.domain.member.entity.Member;
import org.example.domain.question.dto.request.CommentForm;
import org.example.domain.question.dto.request.QuestionCreateForm;
import org.example.domain.question.dto.request.QuestionEditForm;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Question {

    private static final AtomicLong sequence = new AtomicLong();

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Long id;
    private String username;
    private String question_language;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int point;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Comment> comments = new ArrayList<Comment>();

    // 연관관계 편의 메서드
    public void addComment(Comment comment) {
        this.comments.add(comment);
        comment.setQuestion(this);
    }
    // 생성 메서드
    public static Question createQuestion(QuestionCreateForm questionCreateForm, String loggedUsername) {

        return Question.builder()
                .id(sequence.incrementAndGet()) // ID 자동 증가
                .username(loggedUsername)
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



    // 질문 댓글 수정
    public void editComment(CommentForm commentEditForm, Long comment_id) {

    }


    // 질문 삭제
    public Comment deleteComment(Long comment_id){
        Comment mockTest = Comment.builder().comment("mock test").build();
        return mockTest;
    }

}
