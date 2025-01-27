package org.example.domain.global.exception.dto;

import lombok.Getter;
@Getter
public class ExceptionResponse {
    private String name;
    private String message;

    private ExceptionResponse(String name, String message) {
        this.name = name;
        this.message = message;
    }

    public static ExceptionResponse create(final String name, final String message) {
        return new ExceptionResponse(name, message);
    }
}
