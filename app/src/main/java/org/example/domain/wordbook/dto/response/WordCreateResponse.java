package org.example.domain.wordbook.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.wordbook.entity.Word;

@Data
@NoArgsConstructor
public class WordCreateResponse {
    private String word;
    private String mean;

    private WordCreateResponse(Word word) {
        this.word = word.getWord();
        this.mean = word.getMean();
    }

    public static WordCreateResponse create(Word word) {
        return new WordCreateResponse(word);
    }
}
