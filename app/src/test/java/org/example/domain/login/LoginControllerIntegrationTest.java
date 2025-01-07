package org.example.domain.login;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.error.ErrorMsg;
import org.example.domain.login.dto.request.LoginForm;
import org.example.domain.login.dto.response.LoginResponse;
import org.example.domain.member.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.domain.login.LoginTestFixture.*;
import static org.example.domain.member.MemberTestFixture.createMemberForm;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class LoginControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    // 회원 가입
    public ResponseEntity<Member> join() {
        return restTemplate.postForEntity(
                "/profile/add",
                createMemberForm(),
                Member.class);
    }

    // 정상 로그인
    public ResponseEntity<LoginResponse> login() {
        return restTemplate.postForEntity(
                "/login",
                createValidLoginForm(),
                LoginResponse.class);
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    void loginSuccessTest() throws Exception {

        // given
        ResponseEntity<Member> joinResponse = join(); // 회원 가입
        LoginForm validLoginForm = createValidLoginForm(); // 회원 가입한 정보로 로그인
        LoginResponse loginSuccessResponse = loginSuccessResponse(); // 회원 가입한 정보로 로그인 했을 때 기대되는 응답

        //when
        ResponseEntity<LoginResponse> response = restTemplate.postForEntity("/login", validLoginForm, LoginResponse.class);

        //then
        LoginResponse body = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(body).isNotNull();
        assertThat(body.getSessionId()).isNotNull();
        assertThat(body.getUserId()).isEqualTo(loginSuccessResponse.getUserId());
        assertThat(body.getUsername()).isEqualTo(loginSuccessResponse.getUsername());
        assertThat(body.getEmail()).isEqualTo(loginSuccessResponse.getEmail());
        assertThat(body.getMessage()).isEqualTo(loginSuccessResponse.getMessage());

    }

    @Test
    @DisplayName("로그인 실패 테스트 - 비밀번호 불일치")
    void loginFailWrongPasswordTest() throws Exception {

        //given
        ResponseEntity<Member> joinResponse = join(); // 회원 가입
        LoginForm invalidLoginForm = createInvalidLoginForm();// 비밀번호가 다름

        //when
        ResponseEntity<ErrorMsg> response = restTemplate.postForEntity("/login", invalidLoginForm, ErrorMsg.class);

        //then
        ErrorMsg body = response.getBody();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(body.getMsg()).isEqualTo("로그인 실패");
    }

    @Test
    @DisplayName("로그아웃 성공 테스트")
    void logoutTest() throws Exception {
        // Given: 회원가입 및 로그인
        join();
        String sessionId = login().getBody().getSessionId(); // 로그인 후 세션 ID 확보
        log.info(sessionId);

        // 헤더에 세션 ID 추가
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "JSESSIONID=" + sessionId);
        HttpEntity<Void> logoutRequest = new HttpEntity<>(headers);


        // When: 로그아웃 요청
        ResponseEntity<Void> logoutResponse = restTemplate.postForEntity(
                "/logout",
                logoutRequest,
                Void.class
        );

        // Then: 로그아웃 성공 확인
        assertThat(logoutResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("로그아웃 실패 테스트")
    void logoutFailTest() throws Exception {

        // given: 잘못된 세션 ID를 사용해 로그아웃 요청
        String invalidSessionId = "invalid-session-id"; // 유효하지 않은 세션 ID
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "JSESSIONID=" + invalidSessionId); // 잘못된 세션 ID 설정
        HttpEntity<Void> logoutRequest = new HttpEntity<>(headers);

        // When: 로그아웃 요청
        ResponseEntity<ErrorMsg> response = restTemplate.postForEntity(
                "/logout",
                logoutRequest,
                ErrorMsg.class
        );

        // Then: 로그아웃 실패 확인
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getMsg()).contains("세션이 존재하지 않음");
    }
}
