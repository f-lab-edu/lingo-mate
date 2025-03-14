package org.example.domain.wordbook.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.domain.member.entity.Member;
import org.example.domain.wordbook.dto.request.WordbookCreateRequest;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class Wordbook {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wordbook_id")
    private Long id;
    private String title;
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "wordbook", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WordbookWord> wordbookWordList = new ArrayList<>();

    private Wordbook(final WordbookCreateRequest wordbookCreateRequest){
        this.title = wordbookCreateRequest.getTitle();
        this.description = wordbookCreateRequest.getDescription();
    }

    // 연관관계 편의 메서드
    public void addWordbookWord(WordbookWord wordbookWord) {
        wordbookWordList.add(wordbookWord);
        wordbookWord.setWordbook(this);
    }
    public void removeWordbookWord(WordbookWord wordbookWord) {
        wordbookWordList.remove(wordbookWord);
        wordbookWord.setWordbook(null);
    }

    // 생성 메서드
    public static Wordbook createWordbook(WordbookCreateRequest wordbookCreateRequest) {
        return new Wordbook(wordbookCreateRequest);
    }
}
