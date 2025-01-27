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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    public Member member;

    private AuthEntity(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public static AuthEntity createWith(final String refreshToken){
        return new AuthEntity(refreshToken);
    }
    public void setMember(Member member) {
        this.member = member;
        member.setAuthEntity(this);
    }
}
