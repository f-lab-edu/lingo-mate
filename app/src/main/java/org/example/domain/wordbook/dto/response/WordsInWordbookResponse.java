package org.example.domain.wordbook.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.wordbook.entity.Word;

@Data
@NoArgsConstructor
public class WordsInWordbookResponse {
    private Long wordId;
    private String word;
    private String mean;
    private String ex;
    private String imageUrl;

    private WordsInWordbookResponse(Word word) {
        this.wordId = word.getId();
        this.word = word.getWord();
        this.mean = word.getMean();
        this.ex = word.getEx();
        this.imageUrl = word.getImageUrl();
    }

    public static WordsInWordbookResponse create(Word word){
        return new WordsInWordbookResponse(word);
    }
}
