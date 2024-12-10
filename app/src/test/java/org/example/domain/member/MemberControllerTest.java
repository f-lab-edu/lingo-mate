package org.example.domain.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.domain.member.dto.Member;
import org.example.domain.member.dto.MemberEditForm;
import org.example.domain.member.dto.MemberForm;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberController.class)
class MemberControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private MemberRepository memberRepository;
    @Autowired
    private ObjectMapper objectMapper;

    MemberForm createMemberForm() {
        MemberForm memberForm = new MemberForm();
        memberForm.setUsername("chanwu");
        memberForm.setEmail("gdrffg@naver.com");
        memberForm.setPassword("1234");
        memberForm.setNationality("korean");
        memberForm.setNative_lang("english");
        memberForm.setIntroduction("Hello im chanwu");
        return memberForm;
    }

    @Test
    @DisplayName("회원 가입 테스트 - 성공")
    void joinMember() throws Exception{

        //Given
        MemberForm memberForm = createMemberForm();
        Member member = new Member(memberForm);

        //When, Then
        mockMvc.perform(post("/profile/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", memberForm.getUsername())
                .param("email", memberForm.getEmail())
                .param("password", memberForm.getPassword())
                .param("nationality", memberForm.getNationality())
                .param("native_lang", memberForm.getNative_lang())
                .param("introduction", memberForm.getIntroduction()))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("사용자 프로필 조회 테스트")
    void findMemberTest() throws Exception {
        //Given
        MemberForm memberForm = createMemberForm();
        Member member = new Member(memberForm);
        member.setId(1L);

        // Mock 동작 정의
        Mockito.when(memberRepository.findById(1L)).thenReturn(member);

        //When, Then
        mockMvc.perform(get("/profile/{user_id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("chanwu"));
    }

    @Test
    @DisplayName("사용자 프로필 수정 테스트")
    void editMemberTest() throws Exception {
        // Given: Mock 데이터 준비
        MemberForm memberForm = createMemberForm();
        Member member = new Member(memberForm);
        member.setId(1L);

        MemberEditForm editForm = new MemberEditForm();
        editForm.setUsername("NewName");
        editForm.setNationality("USA");
        editForm.setNative_lang("English");
        editForm.setIntroduction("Updated introduction");


        // Mock 설정
        Mockito.when(memberRepository.findById(1L)).thenReturn(member);

        // When, Then
        mockMvc.perform(put("/profile/{user_id}/edit", 1L)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", "NewName")
                        .param("nationality", "USA")
                        .param("native_lang", "English")
                        .param("introduction", "Updated introduction"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("NewName"))
                .andExpect(jsonPath("$.native_lang").value("English"))
                .andExpect(jsonPath("$.introduction").value("Updated introduction"));

    }

}