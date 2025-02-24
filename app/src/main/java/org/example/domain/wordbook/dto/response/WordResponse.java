package org.example.domain.wordbook.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.wordbook.entity.Word;

@Data
@NoArgsConstructor
public class WordResponse {
    private String word;
    private String mean;

    private WordResponse(Word word) {
        this.word = word.getWord();
        this.mean = word.getMean();
    }

    public static WordResponse create(Word word) {
        return new WordResponse(word);
    }
}
