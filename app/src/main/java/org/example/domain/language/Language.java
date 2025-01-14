package org.example.domain.language;

import jakarta.persistence.*;
import lombok.*;
import org.example.domain.member.entity.Member;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Language {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "language_id")
    private Long id;

    private String language;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public static Language createLanguage(String lang) {
        return Language.builder().language(lang).build();
    }
}
