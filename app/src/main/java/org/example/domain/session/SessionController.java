package org.example.domain.session;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class SessionController {

    @GetMapping("/session-info")
    public String sessionInfo(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if(session == null) {
            return "세션이 존재하지 않습니다.";
        }

        session.getAttributeNames().asIterator().forEachRemaining(name -> log.info("name = {}, value = {}", name, session.getAttribute(name)));
        return "세션 정보 출력";
    }
}
