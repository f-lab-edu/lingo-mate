package org.example.domain.wordbook;

import lombok.RequiredArgsConstructor;
import org.example.domain.member.MemberRepository;
import org.example.domain.member.entity.Member;
import org.example.domain.member.exception.MemberNotFound;
import org.example.domain.wordbook.dto.request.WordCreateRequest;
import org.example.domain.wordbook.dto.request.WordbookCreateRequest;
import org.example.domain.wordbook.dto.response.WordbookResponse;
import org.example.domain.wordbook.entity.Word;
import org.example.domain.wordbook.entity.Wordbook;
import org.example.domain.wordbook.entity.WordbookWord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class WordbookService {
    private final WordbookRepository wordbookRepository;
    private final WordRepository wordRepository;
    private final MemberRepository memberRepository;

    // 단어장 생성
    public Wordbook createWordbook(Long memberId, WordbookCreateRequest wordbookCreateRequest) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFound::new);
        Wordbook wordbook = Wordbook.createWordbook(wordbookCreateRequest);
        member.addWordBook(wordbook);
        return wordbookRepository.save(wordbook);
    }

    // 단어장 목록 조회
    public Page<WordbookResponse> findAllWordbook(Pageable pageable) {
        Page<Wordbook> wordbooks = wordbookRepository.findAll(pageable);
        return wordbooks.map(WordbookResponse::create);
    }

    // 단어 생성 및 단어장에 추가
    public Word createAndAddWord(Long wordbookId, WordCreateRequest wordCreateRequest) {
        // 1. 단어장 조회
        Wordbook wordbook = wordbookRepository.findById(wordbookId).orElseThrow();

        // 2. 단어 생성
        Word word = Word.create(wordCreateRequest);

        // 3. 중간 엔티티 생성
        WordbookWord wordbookWord = WordbookWord.create(wordbook, word);

        // 4. 연관관계 설정
        wordbook.addWordbookWord(wordbookWord);
        word.addWordbookWord(wordbookWord);

        // 5. 단어 저장
        return wordRepository.save(word);
    }

    // 단어장 상세 조회
    public void wordbookDetails() {

    }
}
