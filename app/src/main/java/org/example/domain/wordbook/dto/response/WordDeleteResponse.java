package org.example.domain.wordbook.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.wordbook.entity.Word;

@Data
@NoArgsConstructor
public class WordDeleteResponse {
    private Long wordId;
    private String word;

    private WordDeleteResponse(Word word) {
        this.wordId = word.getId();
        this.word = word.getWord();
    }

    public static WordDeleteResponse create(Word word) {
        return new WordDeleteResponse(word);
    }
}
