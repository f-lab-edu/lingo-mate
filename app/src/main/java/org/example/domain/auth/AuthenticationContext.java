package org.example.domain.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Objects;

@Component
@RequestScope
@Slf4j
public class AuthenticationContext {
    private static final String ANONYMOUS_USERNAME = "UNKNOWN";
    private String username;

    public void setAuthentication(String username) {
        this.username = username;
    }

    public void setAnonymousUsername(){
        this.username = ANONYMOUS_USERNAME;
    }

    public String getPrincipal() {
        if (Objects.isNull(this.username)) {
            throw new RuntimeException("username is null");
        }
        log.debug("authcontext username = {}", username);
        return username;
    }
}
