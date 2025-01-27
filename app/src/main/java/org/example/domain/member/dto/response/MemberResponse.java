package org.example.domain.member.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.language.Language;
import org.example.domain.member.entity.Member;

import java.util.List;

@Data
@NoArgsConstructor
public class MemberResponse {
    private Long id;
    private String email;
    private String username;
    private String nationality;
    private String nativeLang;
    private String introduction;
    private List<String> learnings;
    private int follower;
    private int following;
    private int point;
    private String role;

    public static MemberResponse createMemberResponse(Member member) {
        List<Language> learnings = member.getLearnings();
        List<String> languages = learnings.stream().map(Language::getLanguage).toList();

        MemberResponse dto = new MemberResponse();
        dto.id = member.getId();
        dto.email = member.getEmail();
        dto.username = member.getUsername();
        dto.nationality = member.getNationality();
        dto.nativeLang = member.getNativeLang();
        dto.introduction = member.getIntroduction();
        dto.learnings = languages;
        dto.follower = member.getFollower();
        dto.following = member.getFollowing();
        dto.point = member.getPoint();
        dto.role = member.getRole();
        return dto;
    }
}
