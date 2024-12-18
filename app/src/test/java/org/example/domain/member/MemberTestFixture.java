package org.example.domain.member;

import org.example.domain.member.dto.MemberEditForm;
import org.example.domain.member.dto.MemberForm;

import java.util.List;

public class MemberTestFixture {

    // 회원 가입 검증 조건 적합
    public static MemberForm createValidMemberForm() {
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


    // 회원 가입 실패 데이터 (검증 조건 부적합)
    public static MemberForm createInvalidMemberForm() {
        return MemberForm.builder()
                .email("invalid-email")  // 잘못된 이메일 형식
                .username("")  // 비어 있는 사용자 이름
                .password("123")  // 너무 짧은 비밀번호
                .nationality("INVALID_COUNTRY")  // 허용되지 않은 국가 코드
                .native_lang("INVALID_LANGUAGE")  // 허용되지 않은 언어 코드
                .learning(List.of("invalidLang1", "invalidLang2"))  // 잘못된 언어 리스트
                .introduction("This introduction text exceeds the limit of 50 characters because it is too long.")  // 50자를 초과
                .build();
    }


    // 사용자 프로필 수정 데이터
    public static MemberEditForm createValidMemberEditForm() {
        return MemberEditForm.builder()
                .username("updatedUsername")
                .nationality("CAN")
                .native_lang("fr")
                .learning(List.of("es", "cn"))  // 허용된 언어 코드 리스트
                .introduction("This is my updated introduction.")  // 50자 이하
                .build();
    }

    // 사용자 프로필 수정 실패 데이터 (검증 조건 부적함
    public static MemberEditForm createInvalidMemberEditForm() {
        return MemberEditForm.builder()
                .username("updatedUsername")
                .nationality("CAN")
                .native_lang("fr")
                .learning(List.of("es", "it"))  // 지원하지 않는 언어 it 포함
                .introduction("This is my updated introduction.")
                .build();
    }
}
