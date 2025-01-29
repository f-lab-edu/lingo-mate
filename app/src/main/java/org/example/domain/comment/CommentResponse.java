package org.example.domain.comment;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponse {

    private String comment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long memberId;
    private Long questionId;

    private CommentResponse(Comment c){
        this.comment = c.getComment();
        this.createdAt = c.getCreatedAt();
        this.updatedAt = c.getUpdatedAt();
        this.memberId = c.getMember().getId();
        this.questionId = c.getQuestion().getId();
    }

    public static CommentResponse create(Comment c){
        return new CommentResponse(c);
    }
}
