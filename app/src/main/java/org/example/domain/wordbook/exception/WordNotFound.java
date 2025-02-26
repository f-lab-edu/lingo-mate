package org.example.domain.wordbook.exception;

import org.example.domain.global.exception.GlobalException;
import org.springframework.http.HttpStatus;

public class WordNotFound extends GlobalException {

    public WordNotFound() {
        super(HttpStatus.NOT_FOUND, "WORD_NOT_FOUND", "word not found");
    }
}
