package org.example.domain.question.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequest {
    @NotBlank(message = "댓글을 입력하셔야 합니다.")
    @Size(max = 500, message = "댓글은 최대 500자입니다.")
    private String comment;
}
