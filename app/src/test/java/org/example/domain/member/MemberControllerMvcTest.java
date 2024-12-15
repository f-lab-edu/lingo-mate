package org.example.domain.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.domain.member.dto.MemberEditForm;
import org.example.domain.member.dto.MemberForm;
import org.example.domain.member.entity.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.example.domain.member.MemberTestFixture.*;
import static org.example.domain.member.entity.Member.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// 회원 관리 기능 API 통합 테스트
@WebMvcTest(MemberController.class)
class MemberControllerMvcTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MemberService memberService;

    private ObjectMapper objectMapper = new ObjectMapper();


    // 객체를 JSON 문자열로 변환
    String toJson(Object obj) throws Exception {
        return objectMapper.writeValueAsString(obj);
    }

    @BeforeEach
    void createTestData() {
        Member.resetSequence();
    }

    @Test
    @DisplayName("회원 가입 검증 조건을 충족한 회원 가입 성공 테스트")
    void joinMemberSuccess() throws Exception {

        //Given
        MemberForm validMemberForm = createValidMemberForm();
        Mockito.when(memberService.save(validMemberForm)).thenReturn(createMember(validMemberForm));

        //When, Then
        mockMvc.perform(post("/profile/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(validMemberForm)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("valid@example.com"))
                .andExpect(jsonPath("$.username").value("validUsername"))
                .andExpect(jsonPath("$.password").value("validPassword123"))
                .andExpect(jsonPath("$.nationality").value("USA"))
                .andExpect(jsonPath("$.native_lang").value("en"))
                .andExpect(jsonPath("$.learning[0]").value("fr"))
                .andExpect(jsonPath("$.learning[1]").value("ja"))
                .andExpect(jsonPath("$.introduction").value("I am learning languages!"));
    }

    @Test
    @DisplayName("회원 가입 검증 조건을 불충족 회원 가입 실패 테스트")
    void joinMemberFail() throws Exception {

        //Given
        MemberForm invalidMemberForm = createInvalidMemberForm();

        //When, Then
        mockMvc.perform(post("/profile/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(invalidMemberForm)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("사용자 프로필 조회 테스트 - 성공")
    void findMemberTestSuccess() throws Exception {
    
        //Given
        Member member = createMember(createValidMemberForm());
        Mockito.when(memberService.getMember(1L)).thenReturn(member);

        //When, Then
        mockMvc.perform(get("/profile/{user_id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("valid@example.com"))
                .andExpect(jsonPath("$.username").value("validUsername"))
                .andExpect(jsonPath("$.password").value("validPassword123"))
                .andExpect(jsonPath("$.nationality").value("USA"))
                .andExpect(jsonPath("$.native_lang").value("en"))
                .andExpect(jsonPath("$.learning[0]").value("fr"))
                .andExpect(jsonPath("$.learning[1]").value("ja"))
                .andExpect(jsonPath("$.introduction").value("I am learning languages!")); // 소개 확인
    }

    @Test
    @DisplayName("사용자 프로필 조회 테스트 - 실패")
    void findMemberTestFail() throws Exception {
        //Given
        Mockito.when(memberService.getMember(2L)).thenReturn(null);

        //When, Then
        mockMvc.perform(get("/profile/{user_id}", 2L))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("사용자 프로필 수정 테스트 - 성공")
    void editMemberTestSuccess() throws Exception {

        // Given
        Member member = createMember(createValidMemberForm());
        MemberEditForm editForm = createValidMemberEditForm();

        Mockito.when(memberService.updateMember(1L, editForm)).thenReturn(member.editMember(editForm));

        // When, Then
        mockMvc.perform(put("/profile/{user_id}/edit", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(editForm)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("valid@example.com"))
                .andExpect(jsonPath("$.username").value("updatedUsername"))
                .andExpect(jsonPath("$.password").value("validPassword123"))
                .andExpect(jsonPath("$.nationality").value("CAN"))
                .andExpect(jsonPath("$.native_lang").value("fr"))
                .andExpect(jsonPath("$.learning[0]").value("es"))
                .andExpect(jsonPath("$.learning[1]").value("cn"))
                .andExpect(jsonPath("$.introduction").value("This is my updated introduction."));

    }

    @Test
    @DisplayName("사용자 프로필 수정 테스트 - 검증 조건 부족합으로 인한 실패")
    void editMemberTestInvalidFail() throws Exception {

        // Given
        Member member = createMember(createValidMemberForm());
        MemberEditForm invalidMemberEditForm = createInvalidMemberEditForm();


        // When, Then
        mockMvc.perform(put("/profile/{user_id}/edit", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(invalidMemberEditForm)))
                .andExpect(status().isBadRequest());

    }

    @Test
    @DisplayName("사용자 프로필 수정 테스트 - 회원 조회 실패")
    void editMemberTestNotFoundFail() throws Exception {
        MemberEditForm validMemberEditForm = createValidMemberEditForm();
        Mockito.when(memberService.getMember(2L)).thenReturn(null);

        // When, Then
        mockMvc.perform(put("/profile/{user_id}/edit", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(validMemberEditForm)))
                .andExpect(status().isNotFound());

    }

}