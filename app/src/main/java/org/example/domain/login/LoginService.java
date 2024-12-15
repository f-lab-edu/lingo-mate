package org.example.domain.login;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.login.dto.LoginForm;
import org.example.domain.login.dto.LoginResponse;
import org.example.domain.member.MemberRepository;
import org.example.domain.member.entity.Member;
import org.example.domain.session.SessionConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LoginService {

    private MemberRepository memberRepository;

    @Autowired
    public LoginService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
    public LoginResponse createSession(LoginForm loginForm, HttpServletRequest request) {

        Member member = memberRepository.findByEmail(loginForm.getEmail())
                .filter(m -> m.getPassword().equals(loginForm.getPassword()))
                .orElse(null);

        if(member != null) {
            HttpSession session = request.getSession();
            session.setAttribute(SessionConst.LOGIN_MEMBER, member);
            return new LoginResponse(session.getId(), member.getId(), member.getUsername(), member.getEmail());
        }

        // 로그인 실패
        else {
            return new LoginResponse();
        }

    }

    public boolean invalidateSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        log.info("session= {}", session);
        if(session != null) {
            session.invalidate();
            return true;
        } else {
            return false;
        }

    }
}
