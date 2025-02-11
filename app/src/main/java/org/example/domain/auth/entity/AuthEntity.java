package org.example.domain.auth.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.domain.member.entity.Member;

@Entity
@Getter
@NoArgsConstructor
public class AuthEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "auth_id")
    public Long id;
    public String refreshToken;
    public String accessToken;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    public Member member;

    private AuthEntity(String refreshToken, String accessToken) {
        this.refreshToken = refreshToken;
        this.accessToken = accessToken;
    }

    public static AuthEntity createWith(final String refreshToken, final String accessToken){
        return new AuthEntity(refreshToken, accessToken);
    }
    public void setMember(Member member) {
        this.member = member;
        member.setAuthEntity(this);
    }
}
