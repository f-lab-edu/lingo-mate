package org.example.domain.wordbook.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.domain.wordbook.dto.request.WordCreateRequest;
import org.example.domain.wordbook.entity.Wordbook;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "word")
public class Word {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "word_id")
    private Long id;
    private String word;
    private String mean;
    private String ex;
    private String imageUrl;

    @OneToMany(mappedBy = "word", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WordbookWord> wordbookWordList = new ArrayList<>();
    private Word(WordCreateRequest wordCreateRequest) {
        this.word = wordCreateRequest.getWord();
        this.mean = wordCreateRequest.getMean();
        this.ex = wordCreateRequest.getEx();
        this.imageUrl = wordCreateRequest.getImageUrl();
    }

    // 연관관계 편의 메서드
    public void addWordbookWord(WordbookWord wordbookWord) {
        wordbookWordList.add(wordbookWord);
        wordbookWord.setWord(this);
    }

    public void removeWordbookWord(WordbookWord wordbookWord){
        wordbookWordList.remove(wordbookWord);
        wordbookWord.setWord(null);
    }

    // 생성 메서드
    public static Word create(WordCreateRequest wordCreateRequest){
        return new Word(wordCreateRequest);
    }


}
