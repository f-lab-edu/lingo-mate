package org.example.domain.member;

import org.example.domain.member.dto.MemberEditForm;
import org.example.domain.member.dto.MemberForm;
import org.example.domain.member.entity.Member;

import java.util.List;

public class MemberTestFixture {

    // 회원 가입 성공
    public static MemberForm createMemberForm() {
        return MemberForm.builder()
                .email("valid@example.com")  // 유효한 이메일
                .username("validUsername")  // 20자 이하
                .password("validPassword123")  // 8자 이상
                .nationality("USA")
                .native_lang("en")  // 허용된 언어 코드
                .learning(List.of("fr", "ja"))  // 허용된 언어 코드 리스트
                .introduction("I am learning languages!")  // 50자 이하
                .build();
    }

    // 사용자 프로필 수정
    public static MemberEditForm createValidMemberEditForm() {
        return MemberEditForm.builder()
                .username("updatedUsername")
                .nationality("CAN")
                .native_lang("fr")
                .learning(List.of("es", "cn"))  // 허용된 언어 코드 리스트
                .introduction("This is my updated introduction.")  // 50자 이하
                .build();
    }

    // 사용자
    public static Member fakeMember() {
        return Member.builder()
                .id(1L)
                .email("valid@example.com")
                .username("validUsername")
                .password("validPassword123")
                .nationality("USA")
                .native_lang("en")
                .learning(List.of("fr", "ja"))
                .introduction("I am learning languages!")
                .build();
    }

}
