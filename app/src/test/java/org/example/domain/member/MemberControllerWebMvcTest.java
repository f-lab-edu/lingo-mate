package org.example.domain.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.domain.member.dto.request.MemberEditRequest;
import org.example.domain.member.entity.Member;
import org.example.helper.MockBeanInjection;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberController.class)
class MemberControllerWebMvcTest extends MockBeanInjection {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private final String token = "Bearer validToken";
    @Test
    void 프로필을_조회한다() throws Exception {
        //Given & When
        Member member = MemberTestFixture.createMember();
        when(authService.isValidAccessToken(any(String.class))).thenReturn(true);
        when(memberService.findMember(any(Long.class))).thenReturn(CompletableFuture.completedFuture(member));

        //Then
        MvcResult mvcResult = mockMvc.perform(get("/api/profile/1").header(AUTHORIZATION, token))
                .andExpect(status().isOk()).andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("valid@example.com"))
                .andExpect(jsonPath("$.username").value("validUsername"));
    }


    @Test
    void 프로필을_수정한다() throws Exception {
        //Given & When
        Member member = MemberTestFixture.createMember();
        MemberEditRequest memberEditRequest = MemberTestFixture.createMemberEditRequest();
        Member updatedMember = member.editMember(memberEditRequest);
        when(authService.isValidAccessToken(any(String.class))).thenReturn(true);
        when(memberService.modifyMember(any(Long.class), any(MemberEditRequest.class))).thenReturn(CompletableFuture.completedFuture(updatedMember));

        //Then
        MvcResult mvcResult = mockMvc.perform(put("/api/profile/1/edit")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberEditRequest))).andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("updatedUsername"));
    }

}