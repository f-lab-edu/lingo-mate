package org.example.domain.member;

import org.example.domain.language.Language;
import org.example.domain.member.dto.request.MemberEditRequest;
import org.example.domain.member.dto.request.MemberJoinRequest;
import org.example.domain.member.dto.response.MemberResponse;
import org.example.domain.member.entity.Member;

import java.util.ArrayList;
import java.util.List;


public class MemberTestFixture {

    // 회원 가입 요청
    public static MemberJoinRequest createMemberJoinRequest() {
        return MemberJoinRequest.builder()
                .email("valid@example.com")  // 유효한 이메일
                .username("validUsername")  // 20자 이하
                .password("validPassword123")  // 8자 이상
                .nationality("USA")
                .nativeLang("en")  // 허용된 언어 코드
                .learning(List.of("fr", "ja"))  // 허용된 언어 코드 리스트
                .introduction("I am learning languages!")  // 50자 이하
                .build();
    }

    // 회원 가입 성공
    public static MemberResponse createMemberResponse() {
        return MemberResponse.createMemberResponse(createMember());
    }

    // 사용자 프로필 수정
    public static MemberEditRequest createMemberEditRequest() {
        return MemberEditRequest.builder()
                .username("updatedUsername")
                .nationality("CAN")
                .nativeLang("fr")
                .learning(List.of("es", "cn"))  // 허용된 언어 코드 리스트
                .introduction("This is my updated introduction.")  // 50자 이하
                .build();
    }

    public static Member createByMysqlMember() {
        return Member.createMember(MemberTestFixture.createMemberJoinRequest());
    }
    // 사용자 생성
    public static Member createMember( ){
        Member member = Member.createMember(MemberTestFixture.createMemberJoinRequest());
        member.setId(1L);
        member.setLearnings(new ArrayList<>(List.of(Language.createLanguage("fr"), Language.createLanguage("ja"))));
        return member;
    }
}
