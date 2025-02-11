package org.example.domain.comment;

import jakarta.persistence.*;
import lombok.*;
import org.example.domain.member.entity.Member;
import org.example.domain.question.dto.request.CommentRequest;
import org.example.domain.question.entity.Question;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter @Setter
public class Comment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;
    private String comment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    private Comment(CommentRequest commentRequest){
        this.comment = commentRequest.getComment();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // 생성 메서드
    public static Comment createComment(CommentRequest commentRequest) {
        return new Comment(commentRequest);
    }

    // 수정 메서드
    public Comment editComment(CommentRequest commentRequest) {
        this.comment = commentRequest.getComment();
        this.updatedAt = LocalDateTime.now();
        return this;
    }

}