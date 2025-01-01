package org.example.domain.login;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.login.dto.request.LoginForm;
import org.example.domain.login.dto.response.LoginResponse;
import org.example.domain.member.MemberRepository;
import org.example.domain.member.entity.Member;
import org.example.domain.session.SessionConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class LoginService {

    private MemberRepository memberRepository;

    @Autowired
    public LoginService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public LoginResponse createSession(LoginForm loginForm, HttpServletRequest request) {

        Optional<Member> optionalMember = memberRepository.findByEmailAndPassword(loginForm.getEmail(), loginForm.getPassword());

        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            HttpSession session = request.getSession();
            session.setAttribute(SessionConst.LOGIN_MEMBER, member);

            return LoginResponse.builder()
                    .sessionId(session.getId())
                    .userId(member.getId())
                    .username(member.getUsername())
                    .email(member.getEmail())
                    .message("login success")
                    .build();
        }

        // 로그인 실패 "login fail"
        else {
            throw new RuntimeException("로그인 실패");
        }

    }

    public void invalidateSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        log.debug("session= {}", session);

        if (session == null) {
            throw new RuntimeException("세션이 존재하지 않음");
        }

        log.debug("로그아웃 시도");
        session.invalidate();
    }
}
