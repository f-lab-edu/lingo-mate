package org.example.domain.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.domain.auth.dto.request.LoginRequest;
import org.example.domain.auth.dto.request.RefreshRequest;
import org.example.domain.auth.dto.response.TokenResponse;
import org.example.domain.auth.fixture.AuthTestFixture;
import org.example.domain.member.MemberTestFixture;
import org.example.domain.member.dto.request.MemberJoinRequest;
import org.example.domain.member.entity.Member;
import org.example.domain.member.entity.Role;
import org.example.helper.MockBeanInjection;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;

import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
public class AuthControllerWebMvcTest extends MockBeanInjection {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void 회원가입을_한다()throws Exception{
        // given
        MemberJoinRequest memberJoinRequest = MemberTestFixture.createMemberJoinRequest();
        Member member = MemberTestFixture.createMember();

        when(authService.addMember(any(MemberJoinRequest.class))).thenReturn(CompletableFuture.completedFuture(member));

        // When & Then
        MvcResult mvcResult = mockMvc.perform(post("/auth/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberJoinRequest)))
                .andExpect(status().isOk()).andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("valid@example.com"))
                .andExpect(jsonPath("$.username").value("validUsername"))
                .andExpect(jsonPath("$.nationality").value("USA"))
                .andExpect(jsonPath("$.nativeLang").value("en"))
                .andExpect(jsonPath("$.learnings[0]").value("fr"))
                .andExpect(jsonPath("$.learnings[1]").value("ja"))
                .andExpect(jsonPath("$.introduction").value("I am learning languages!"))
                .andExpect(jsonPath("$.follower").value(0))
                .andExpect(jsonPath("$.following").value(0))
                .andExpect(jsonPath("$.point").value(50))
                .andExpect(jsonPath("$.role").value("USER"));

    }

    @Test
    void 로그인을_한다() throws Exception{
        // given
        LoginRequest loginRequest = AuthTestFixture.createLoginRequest();
        TokenResponse tokenResponse = AuthTestFixture.createTokenResponse();
        when(authService.issueToken(any(LoginRequest.class))).thenReturn(CompletableFuture.completedFuture(tokenResponse));

        // When & Then
        MvcResult mvcResult = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk()).andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(header().string("Authorization", "Bearer " + tokenResponse.getAccessToken()))
                .andExpect(jsonPath("$.accessToken").value(tokenResponse.getAccessToken()))
                .andExpect(jsonPath("$.refreshToken").value(tokenResponse.getRefreshToken()));
    }

    @Test
    void accessToken을_재발급_받는다() throws Exception{

        //Given
        RefreshRequest refreshRequest = AuthTestFixture.createRefreshRequest();
        TokenResponse tokenResponse = AuthTestFixture.createTokenResponse();
        when(authService.reissueRefreshToken(refreshRequest)).thenReturn(CompletableFuture.completedFuture(tokenResponse));



        //When & Then
        MvcResult mvcResult = mockMvc.perform(post("/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(refreshRequest)))
                .andExpect(status().isOk()).andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(header().string("Authorization", "Bearer " + tokenResponse.getAccessToken()))
                .andExpect(jsonPath("$.accessToken").value(tokenResponse.getAccessToken()))
                .andExpect(jsonPath("$.refreshToken").value(tokenResponse.getRefreshToken()));
    }


}
