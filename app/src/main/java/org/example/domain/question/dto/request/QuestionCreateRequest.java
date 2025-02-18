package org.example.domain.question.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionCreateRequest {

    @NotBlank(message = "질문 언어를 입력하셔야 합니다.")
    @Pattern(
            regexp = "^(ko|en|ja|cn|fr|ar|es|ru)$",
            message = "허용되지 않은 언어 코드입니다. (ko, en, ja, cn, fr, ar, es, ru만 허용)"
    )
    private String questionLanguage;

    @NotBlank(message = "제목은 반드시 입력해야 합니다.")
    @Size(max = 50, message = "제목은 최대 50자입니다.")
    private String title;

    @NotBlank(message = "질문 내용을 반드시 입력해야 합니다.")
    @Size(max = 500, message = "질문 내용은 최대 500자입니다.")
    private String content;

    @NotNull(message = "포인트를 입력하셔야 합니다.")
    @Min(value = 0, message = "포인트는 최소 0이어야 합니다.")
    @Max(value = 100, message = "포인트는 최대 100이어야 합니다.")
    private Integer point;
}
