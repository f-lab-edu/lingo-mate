package org.example.domain.wordbook.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
@Table(name = "wordbook_word")
public class WordbookWord {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wordbook_id")
    private Wordbook wordbook;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "word_id")
    private Word word;

    private WordbookWord(Wordbook wordbook, Word word) {
        this.wordbook = wordbook;
        this.word = word;
    }

    // 생성 메서드
    public static WordbookWord create(Wordbook wordbook, Word word) {
        return new WordbookWord(wordbook, word);
    }
}
