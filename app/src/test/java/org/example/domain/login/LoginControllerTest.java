package org.example.domain.login;

import org.example.domain.login.dto.LoginForm;
import org.example.domain.member.MemberRepository;
import org.example.domain.member.dto.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LoginController.class)
class LoginControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MemberRepository memberRepository;

    @Test
    @DisplayName("로그인 성공 테스트")
    void loginSuccessTest() throws Exception {
        LoginForm loginForm = new LoginForm();
        loginForm.setEmail("gdrffg@naver.com");
        loginForm.setPassword("1234");

        Member member = new Member();
        member.setId(1L);
        member.setEmail(loginForm.getEmail());
        member.setPassword(loginForm.getPassword());

        Mockito.when(memberRepository.findByEmail("gdrffg@naver.com")).thenReturn(Optional.of(member));

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("email", "gdrffg@naver.com")
                .param("password", "1234"))
                .andExpect(status().isOk())
                .andExpect(content().string("ok"));
    }

    @Test
    @DisplayName("로그인 실패 테스트 - 비밀번호 불일치")
    void loginFailWrongPasswordTest() throws Exception {
        LoginForm loginForm = new LoginForm();
        loginForm.setEmail("gdrffg@naver.com");
        loginForm.setPassword("1234");

        Member member = new Member();
        member.setId(1L);
        member.setEmail(loginForm.getEmail());
        member.setPassword(loginForm.getPassword());

        Mockito.when(memberRepository.findByEmail("gdrffg@naver.com")).thenReturn(Optional.of(member));

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("email", "gdrffg@naver.com")
                        .param("password", "wrong"))
                .andExpect(status().isOk())
                .andExpect(content().string("not ok"));
    }

    @Test
    @DisplayName("로그인 실패 테스트 - 비밀번호 불일치")
    void loginFailNoUserTest() throws Exception {
        LoginForm loginForm = new LoginForm();
        loginForm.setEmail("gdrffg@naver.com");
        loginForm.setPassword("1234");

        Member member = new Member();
        member.setId(1L);
        member.setEmail(loginForm.getEmail());
        member.setPassword(loginForm.getPassword());

        Mockito.when(memberRepository.findByEmail("gdrffg@naver.com")).thenReturn(Optional.of(member));

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("email", "wrong@naver.com")
                        .param("password", "1234"))
                .andExpect(status().isOk())
                .andExpect(content().string("not ok"));
    }

    @Test
    @DisplayName("로그아웃 테스트")
    void loginOutTest() throws Exception {
        mockMvc.perform(post("/logout"))
                .andExpect(status().isOk())
                .andExpect(content().string("ok"));
    }
}