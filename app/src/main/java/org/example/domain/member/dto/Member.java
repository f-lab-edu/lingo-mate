package org.example.domain.member.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Member {
    private Long id;
    private String email;
    private String username;
    private String password;
    private String nationality;
    private String native_lang;
    private List<String> learning;
    private String introduction;
    private Integer follower;
    private Integer following;
    private Integer point;
    private Integer credibility;

    private static long sequence = 0L;

    // 회원 가입 정보 memberForm 을 이용하여 사용자 정보 생성
    public Member(MemberForm memberForm) {
        this.id = ++sequence;
        this.email = memberForm.getEmail();
        this.username = memberForm.getUsername();
        this.password = memberForm.getPassword();
        this.nationality = memberForm.getNationality();
        this.native_lang = memberForm.getNative_lang();
        this.introduction = memberForm.getIntroduction();
        this.learning = memberForm.getLearning();
        this.follower = 0;
        this.following = 0;
        this.point = 50;
        this.credibility = 50;
    }

    // memberEditFrom 을 이용하여 사용자 정보 수정
    public Member editMember(MemberEditForm memberEditForm) {
        this.username = memberEditForm.getUsername();
        this.nationality = memberEditForm.getNationality();
        this.native_lang = memberEditForm.getNative_lang();
        this.learning = memberEditForm.getLearning();
        this.introduction = memberEditForm.getIntroduction();
        return this;
    }
}
