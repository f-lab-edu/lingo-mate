package org.example.domain.wordbook.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.wordbook.entity.Word;

@Data
@NoArgsConstructor
public class WordUpdateRequest {
    private Long wordId;
    private String word;
    private String mean;
    private String ex;
    private String imageUrl;
}
