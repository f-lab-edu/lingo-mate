package org.example.domain.question.dto.response;

import lombok.Getter;
import org.example.domain.comment.Comment;

import java.time.LocalDateTime;

@Getter
public class CommentResponse {
    private Long commentId;
    private String comment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long memberId;
    private Long questionId;

    private CommentResponse(Comment c){
        this.commentId = c.getId();
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
