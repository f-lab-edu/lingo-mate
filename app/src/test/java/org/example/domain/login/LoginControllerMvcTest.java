package org.example.domain.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.login.dto.LoginForm;
import org.example.domain.login.dto.LoginResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.example.domain.login.LoginTestFixture.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@WebMvcTest(LoginController.class)
class LoginControllerMvcTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private LoginService loginService;
    private ObjectMapper objectMapper = new ObjectMapper();

    private String toJson(Object obj) throws Exception{
        return objectMapper.writeValueAsString(obj);
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    void loginSuccessTest() throws Exception {
        // Given
        LoginForm validLoginForm = createValidLoginForm();
        LoginResponse loginSuccessResponse = loginSuccessResponse();
        Mockito.when(loginService.createSession(Mockito.any(), Mockito.any()))
               .thenReturn(loginSuccessResponse);

        // When & Then
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(validLoginForm)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sessionId").value(loginSuccessResponse.getSessionId()))
                .andExpect(jsonPath("$.userId").value(loginSuccessResponse.getUserId()))
                .andExpect(jsonPath("$.username").value(loginSuccessResponse.getUsername()))
                .andExpect(jsonPath("$.email").value(loginSuccessResponse.getEmail()));
    }

    @Test
    @DisplayName("로그인 실패 테스트")
    void loginFailWrongPasswordTest() throws Exception {
        // Given
        LoginForm invalidLoginForm = createInvalidLoginForm();
        LoginResponse loginResponse = loginFailResponse();

        Mockito.when(loginService.createSession(Mockito.any(), Mockito.any()))
               .thenReturn(loginResponse);

        // When & Then
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(invalidLoginForm)))
                .andExpect(status().isBadRequest());
    }



    @Test
    @DisplayName("로그아웃 성공 테스트")
    void logoutTest() throws Exception {

        // Given
        Mockito.when(loginService.invalidateSession(Mockito.any()))
               .thenReturn(true);

        // When & Then
        mockMvc.perform(post("/logout")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("로그아웃 성공"));
    }

    @Test
    @DisplayName("로그아웃 실패 테스트")
    void logoutFailTest() throws Exception {
        // Given
        Mockito.when(loginService.invalidateSession(Mockito.any()))
                .thenReturn(false);

        // When & Then
        mockMvc.perform(post("/logout")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("로그아웃 실패"));

    }
}