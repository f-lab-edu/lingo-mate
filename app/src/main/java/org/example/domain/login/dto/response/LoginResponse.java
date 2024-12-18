package org.example.domain.login.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String sessionId;
    private Long userId;
    private String username;
    private String email;
    private String message;

    public LoginResponse(String message) {
        this.message = message;
    }
}
