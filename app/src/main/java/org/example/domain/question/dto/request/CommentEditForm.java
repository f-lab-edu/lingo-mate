package org.example.domain.question.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentEditForm {
    @NotBlank(message = "댓글을 입력하셔야 합니다.")
    @Size(max = 500, message = "댓글은 최대 500자입니다.")
    private String comment;
}
