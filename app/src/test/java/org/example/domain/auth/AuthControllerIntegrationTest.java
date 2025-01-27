package org.example.domain.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.example.domain.auth.dto.request.LoginRequest;
import org.example.domain.auth.dto.response.TokenResponse;
import org.example.domain.auth.fixture.AuthTestFixture;
import org.example.domain.member.MemberRepository;
import org.example.domain.member.MemberTestFixture;
import org.example.domain.member.dto.request.MemberJoinRequest;
import org.example.domain.member.dto.response.MemberResponse;
import org.example.domain.member.entity.Member;
import org.example.domain.member.entity.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class AuthControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MemberRepository memberRepository;

    private final RestTemplate restTemplate = new RestTemplate();
    @BeforeEach
    public void setUp() {
        memberRepository.deleteAll();
        em.flush();
        em.clear();
    }

    @Test
    void 회원가입_성공을_확인한다() throws Exception {
        // given
        MemberJoinRequest memberJoinRequest = MemberTestFixture.createMemberJoinRequest();
        String url = "http://localhost:" + port + "/auth/join";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(memberJoinRequest), headers);

        // When
        ResponseEntity<MemberResponse> response = restTemplate.exchange(url, HttpMethod.POST, request, MemberResponse.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        MemberResponse body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getNationality()).isEqualTo("USA");
        assertThat(body.getNativeLang()).isEqualTo("en");
        assertThat(body.getLearnings()).containsExactly("fr", "ja");
        assertThat(body.getIntroduction()).isEqualTo("I am learning languages!");
        assertThat(body.getFollower()).isEqualTo(0);
        assertThat(body.getFollowing()).isEqualTo(0);
        assertThat(body.getPoint()).isEqualTo(50);
        assertThat(body.getRole()).isEqualTo(Role.USER);
    }

    //@Test
    void 로그인_성공을_확인한다() throws Exception{
        // given

        // 회원 저장 org.springframework.orm.ObjectOptimisticLockingFailureException
        Member member = MemberTestFixture.createMember();
        memberRepository.saveAndFlush(member);

        LoginRequest loginRequest = AuthTestFixture.createLoginRequest();
        TokenResponse tokenResponse = AuthTestFixture.createTokenResponse();
        String url = "http://localhost:" + port + "/auth/login";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(loginRequest), headers);

        // When
        ResponseEntity<TokenResponse> response = restTemplate.exchange(url, HttpMethod.POST, request, TokenResponse.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        TokenResponse body = response.getBody();

        assertThat(body).isNotNull();

    }


}

