package org.example.domain.member.dto.response;

import lombok.Builder;
import lombok.Data;
import org.example.domain.language.Language;
import org.example.domain.member.entity.Member;

import java.util.List;

@Data
@Builder
public class MemberDTO {
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

    public static MemberDTO createMemberDTO(Member member) {
        List<Language> learnings = member.getLearnings();
        List<String> languages = learnings.stream().map(Language::getLanguage).toList();

        return MemberDTO.builder()
                .id(member.getId())
                .email(member.getEmail())
                .username(member.getUsername())
                .nationality(member.getNationality())
                .nativeLang(member.getNativeLang())
                .introduction(member.getIntroduction())
                .learnings(languages)
                .follower(member.getFollower())
                .following(member.getFollowing())
                .point(member.getPoint())
                .build();
    }
}
