package org.example.domain.question.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.example.domain.member.entity.Member;
import org.example.domain.question.dto.request.CommentForm;

import java.time.LocalDateTime;

@Data
@Builder
public class Comment {
    private static Long sequence = 0L;

    private Long id;
    private String comment;
    private Long memberId;
    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Comment createComment(CommentForm commentForm, Member member) {
        return Comment.builder().id(++sequence)
                .comment(commentForm.getComment())
                .memberId(member.getId())
                .username(member.getUsername())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public Comment editComment(CommentForm commentForm) {
        this.comment = commentForm.getComment();
        this.updatedAt = LocalDateTime.now();
        return this;
    }

}