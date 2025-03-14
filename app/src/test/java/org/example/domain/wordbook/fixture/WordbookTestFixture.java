package org.example.domain.wordbook.fixture;

import org.example.domain.wordbook.dto.request.WordCreateRequest;
import org.example.domain.wordbook.dto.request.WordDeleteRequest;
import org.example.domain.wordbook.dto.request.WordUpdateRequest;
import org.example.domain.wordbook.dto.request.WordbookCreateRequest;
import org.example.domain.wordbook.entity.Word;
import org.example.domain.wordbook.entity.Wordbook;

public class WordbookTestFixture {

    public static WordbookCreateRequest createWordbookCreateRequest() {
        return new WordbookCreateRequest("title", "description");
    }

    public static WordCreateRequest createWordRequest( ){
        return new WordCreateRequest("word", "mean", "ex", "image");
    }

    public static WordDeleteRequest createWordDeleteRequest(){
        return new WordDeleteRequest(1L);
    }

    public static WordUpdateRequest createWordUpdateRequest() {
        return new WordUpdateRequest(1L, "new word", "new mean", "new ex", "new image");
    }

    public static Wordbook createWordbook() {
        Wordbook wordbook = Wordbook.createWordbook(createWordbookCreateRequest());
        wordbook.setId(1L);
        return wordbook;
    }
    public static Word createWord() {
        Word word = Word.create(createWordRequest());
        word.setId(1L);
        return word;
    }
}
