package org.example.domain.wordbook.exception;

import org.example.domain.global.exception.GlobalException;
import org.springframework.http.HttpStatus;

public class WordNotExistInWordbook extends GlobalException {
    public WordNotExistInWordbook() {
        super(HttpStatus.NOT_FOUND, "WORD_NOT_EXIST_IN_WORDBOOK", "word does not exist in the wordbook");
    }
}
