package org.example.domain.question.fixture;

import org.example.domain.comment.Comment;
import org.example.domain.question.dto.request.CommentRequest;

public class CommentTestFixture {
    public static CommentRequest createCommentRequest(){
        return new CommentRequest("댓글1");
    }

    public static CommentRequest createEditCommentRequest(){
        return new CommentRequest("수정 댓글1");
    }

    public static Comment createComment() {
        // 생성
        Comment comment = Comment.createComment(createCommentRequest());
        comment.setId(0L);

        return comment;
    }
}
