package org.example.domain.member.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.member.dto.MemberEditForm;
import org.example.domain.member.dto.MemberForm;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Data
@Builder
public class Member {

    private static final AtomicLong sequence = new AtomicLong();

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
    // ID 초기화 메서드
    public static void resetSequence() {
        sequence.set(0);
    }
    // 회원 가입 정보 memberForm 을 이용하여 사용자 정보 생성
    public static Member createMember(MemberForm memberForm) {
        return Member.builder()
                .id(sequence.incrementAndGet())
                .email(memberForm.getEmail())
                .username(memberForm.getUsername())
                .password(memberForm.getPassword())
                .nationality(memberForm.getNationality())
                .native_lang(memberForm.getNative_lang())
                .learning(memberForm.getLearning())
                .introduction(memberForm.getIntroduction())
                .follower(0)
                .following(0)
                .point(50)
                .credibility(50)
                .build();
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
