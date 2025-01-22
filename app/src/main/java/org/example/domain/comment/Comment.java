package org.example.domain.comment;

import jakarta.persistence.*;
import lombok.*;
import org.example.domain.member.entity.Member;
import org.example.domain.question.dto.request.CommentRequest;
import org.example.domain.question.entity.Question;

import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Comment {
    private static Long sequence = 0L;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;
    private String comment;
    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    public static Comment createComment(CommentRequest commentRequest, Member member) {
        return Comment.builder().id(++sequence)
                .comment(commentRequest.getComment())
                .build();
    }

    public Comment editComment(CommentRequest commentRequest) {
        this.comment = commentRequest.getComment();
        this.updatedAt = LocalDateTime.now();
        return this;
    }

}