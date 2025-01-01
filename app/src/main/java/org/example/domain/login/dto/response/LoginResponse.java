package org.example.domain.login.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class LoginResponse {
    private String sessionId;
    private Long userId;
    private String username;
    private String email;
    private String message;
}
