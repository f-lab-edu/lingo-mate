package org.example.domain.wordbook.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WordCreateRequest {
    private String word;
    private String mean;
    private String ex;
    private String imageUrl;
}
