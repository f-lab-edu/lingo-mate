package org.example.domain.wordbook.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WordbookCreateRequest {

    @NotBlank(message = "제목은 반드시 입력해야 합니다.")
    @Size(max = 50, message = "제목은 최대 50자입니다.")
    private String title;

    @Size(max = 50, message = "설명은 최대 100자입니다.")
    private String description;
}
