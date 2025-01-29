package org.example.domain.question.dto.request;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuestionEditRequest {
    @Pattern(
            regexp = "^(ko|en|ja|cn|fr|ar|es|ru)$",
            message = "허용되지 않은 언어 코드입니다. (ko, en, ja, cn, fr, ar, es, ru만 허용)"
    )
    private String questionLanguage;

    @Size(max = 50, message = "제목은 최대 50자입니다.")
    private String title;

    @Size(max = 500, message = "질문 내용은 최대 500자입니다.")
    private String content;

    @Min(value = 0, message = "포인트는 최소 0이어야 합니다.")
    @Max(value = 100, message = "포인트는 최대 100이어야 합니다.")
    private Integer point;
}
