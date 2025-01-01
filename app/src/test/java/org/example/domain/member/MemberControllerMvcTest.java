package org.example.domain.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.domain.member.dto.MemberEditForm;
import org.example.domain.member.dto.MemberForm;
import org.example.domain.member.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.example.domain.member.MemberTestFixture.*;
import static org.example.domain.member.entity.Member.createMember;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// 회원 관리 기능 API 통합 테스트
@WebMvcTest(MemberController.class)
@ActiveProfiles("test")
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
    /*
    @BeforeEach
    void createTestData() {
        Member.resetSequence();
    }
     */
    @Test
    void 회원가입_성공() throws Exception {

        //Given
        MemberForm validMemberForm = createMemberForm();
        Mockito.when(memberService.addMember(validMemberForm)).thenReturn(createMember(validMemberForm));

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
    void 프로필_조회_성공() throws Exception {
    
        //Given
        Member member = fakeMember();
        Mockito.when(memberService.findMember(1L)).thenReturn(member);

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
    void 프로필_조회_실패_존재하지_않는_회원() throws Exception {
        //Given
        Mockito.when(memberService.findMember(2L)).thenThrow(RuntimeException.class);

        //When, Then
        mockMvc.perform(get("/profile/{user_id}", 2L))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 프로필_수정_성공() throws Exception {

        // Given
        Member member = fakeMember();
        MemberEditForm editForm = createValidMemberEditForm();

        Mockito.when(memberService.modifyMember(1L, editForm)).thenReturn(member.editMember(editForm));

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
    @DisplayName("")
    void 프로필_수정_실패_존재하지_않는_회원() throws Exception {
        //Given
        MemberEditForm validMemberEditForm = createValidMemberEditForm();
        Mockito.when(memberService.modifyMember(2L, validMemberEditForm)).thenThrow(RuntimeException.class);

        // When, Then
        mockMvc.perform(put("/profile/{user_id}/edit", 2L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(validMemberEditForm)))
                .andExpect(status().isBadRequest());

    }

}