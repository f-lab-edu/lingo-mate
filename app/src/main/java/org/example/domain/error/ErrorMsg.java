package org.example.domain.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorMsg {
    private String msg;
    private String errorType;
}
