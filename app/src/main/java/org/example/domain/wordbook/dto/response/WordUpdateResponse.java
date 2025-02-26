package org.example.domain.wordbook.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.domain.wordbook.entity.Word;

@Getter
@NoArgsConstructor
public class WordUpdateResponse {
    private String word;
    private String mean;
    private String ex;
    private String imageUrl;

    private WordUpdateResponse(Word word) {
        this.word = word.getWord();
        this.mean = word.getMean();
        this.ex = word.getEx();
        this.imageUrl = word.getImageUrl();
    }

    public static WordUpdateResponse create(Word word){
        return new WordUpdateResponse(word);
    }
}
