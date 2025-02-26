package org.example.domain.wordbook.exception;

import org.example.domain.global.exception.GlobalException;
import org.springframework.http.HttpStatus;

public class WordbookNotFound extends GlobalException {
    public WordbookNotFound() {
        super(HttpStatus.NOT_FOUND, "WORDBOOK_NOT_FOUND", "wordbook not found");
    }
}
