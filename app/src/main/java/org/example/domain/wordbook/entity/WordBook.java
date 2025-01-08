package org.example.domain.wordbook.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.domain.member.entity.Member;
import org.example.domain.word.Word;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class WordBook {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wordbook_id")
    private Long id;
    private String title;
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "wordbook", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Word> words = new ArrayList<Word>();

    // 연관관계 편의 메서드
    public void addWord(Word word) {
        this.words.add(word);
        word.setWordbook(this);
    }
}
