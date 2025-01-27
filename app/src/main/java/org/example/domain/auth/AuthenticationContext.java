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
    private Long acceptedMemberId;

    public void setAuthentication(Long acceptedMemberId) {
        this.acceptedMemberId = acceptedMemberId;
    }

    public void setAnonymousUsername(){
        this.acceptedMemberId = 0L;
    }

    public Long getPrincipal() {
        if (Objects.isNull(this.acceptedMemberId)) {
            throw new RuntimeException("acceptedMemberId is null");
        }
        log.debug("authcontext acceptedMemberId = {}", acceptedMemberId);
        return acceptedMemberId;
    }
}
