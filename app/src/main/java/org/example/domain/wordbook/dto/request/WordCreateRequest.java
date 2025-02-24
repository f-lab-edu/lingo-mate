package org.example.domain.wordbook.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WordCreateRequest {
    private String word;
    private String mean;
    private String ex;
    private String imageUrl;
}
