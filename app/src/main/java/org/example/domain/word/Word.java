package org.example.domain.word;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.domain.wordbook.entity.WordBook;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "words")
public class Word {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "word_id")
    private Long id;
    private String word;
    private String mean;
    private String example;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wordbook_id")
    private WordBook wordbook;
}
