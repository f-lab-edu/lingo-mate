package org.example.domain.wordbook.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WordDeleteRequest {
    private Long wordId;
}
