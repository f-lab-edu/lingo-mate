package org.example.domain.wordbook;

import lombok.RequiredArgsConstructor;
import org.example.domain.member.MemberRepository;
import org.example.domain.member.entity.Member;
import org.example.domain.member.exception.MemberNotFound;
import org.example.domain.wordbook.dto.request.WordCreateRequest;
import org.example.domain.wordbook.dto.request.WordDeleteRequest;
import org.example.domain.wordbook.dto.request.WordUpdateRequest;
import org.example.domain.wordbook.dto.request.WordbookCreateRequest;
import org.example.domain.wordbook.dto.response.WordUpdateResponse;
import org.example.domain.wordbook.dto.response.WordbookResponse;
import org.example.domain.wordbook.entity.Word;
import org.example.domain.wordbook.entity.Wordbook;
import org.example.domain.wordbook.entity.WordbookWord;
import org.example.domain.wordbook.exception.AccessNotAllowed;
import org.example.domain.wordbook.exception.WordNotExistInWordbook;
import org.example.domain.wordbook.exception.WordNotFound;
import org.example.domain.wordbook.exception.WordbookNotFound;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Transactional
@Service
public class WordbookService {

    private final WordbookRepository wordbookRepository;
    private final WordbookWordRepository wordbookWordRepository;
    private final WordRepository wordRepository;
    private final MemberRepository memberRepository;

    // 단어장 생성
    public CompletableFuture<Wordbook> createWordbook(Long memberId, WordbookCreateRequest wordbookCreateRequest) {

        return CompletableFuture.supplyAsync(() -> {
            Member member = memberRepository.findByIdWithWordbooks(memberId).orElseThrow(MemberNotFound::new);
            Wordbook wordbook = Wordbook.createWordbook(wordbookCreateRequest);
            member.addWordBook(wordbook);
            return wordbookRepository.save(wordbook);
        });
    }

    // 단어장 삭제
    public CompletableFuture<Wordbook> deleteWordbook(Long memberId, Long wordbookId) {

        return CompletableFuture.supplyAsync(() -> {
            // 1. 단어장을 조회한다.
            Wordbook wordbook = wordbookRepository.findById(wordbookId).orElseThrow(WordbookNotFound::new);
            // 2. 로그인 한 멤버의 id와 단어장 소유자의 id가 다르면 접근 권한이 없다
            if(wordbook.getMember().getId() != memberId) {
                throw new AccessNotAllowed();
            }

            // 3. 단어장으로 중계 테이블 정보를 조회한다.
            List<WordbookWord> wordbookWords = wordbookWordRepository.findAllByWordbook(wordbook);

            List<Word> words = wordbookWords.stream().map(wordbookWord -> {
                // 4. 중계 테이블 정보로 단어를 찾는다.
                Word word = wordRepository.findById(wordbookWord.getWord().getId()).orElseThrow(WordNotFound::new);
                // 5. 찾은 단어로 중계 테이블과의 연간 관계를 해제하여 중계 테이블 정보를 삭제한다.
                word.removeWordbookWord(wordbookWord);
                return word;
            }).toList();

            // 6. 삭제할 단어장에 있는 단어를 삭제한다.
            wordRepository.deleteAll(words);

            // 7. 단어장을 삭제한다.
            wordbookRepository.delete(wordbook);
            return wordbook;
        });
    }

    // 단어장 목록 조회
    public CompletableFuture<Page<WordbookResponse>> findAllWordbook(Pageable pageable) {
        return CompletableFuture.supplyAsync(() -> {
            Page<Wordbook> wordbooks = wordbookRepository.findAll(pageable);
            return wordbooks.map(WordbookResponse::create);
        });
    }

    // 단어 생성 및 단어장에 추가
    public CompletableFuture<List<Word>> createAndAddWord(Long wordbookId, List<WordCreateRequest> wordCreateRequestList) {
        return CompletableFuture.supplyAsync(() -> {
            // 1. 단어장 조회
            Wordbook wordbook = wordbookRepository.findById(wordbookId).orElseThrow();

            // 2. 단어 생성
            List<Word> createdWords = wordCreateRequestList.stream().map(Word::create).toList();

            for (Word createdWord : createdWords) {
                // 3. 중간 엔티티 생성
                WordbookWord wordbookWord = WordbookWord.create(wordbook, createdWord);

                // 4. 연관관계 설정
                wordbook.addWordbookWord(wordbookWord);
                createdWord.addWordbookWord(wordbookWord);
            }

            // 5. 단어 저장
            return wordRepository.saveAll(createdWords);
        });
    }

    // 단어장 상세 조회
    public CompletableFuture<List<Word>> getWordsInWordbook(Long wordbookId) {

        return CompletableFuture.supplyAsync(() -> {
            // 1. 단어장 존재 여부 확인
            Wordbook wordbook = wordbookRepository.findById(wordbookId).orElseThrow(WordbookNotFound::new);

            // 2. 단어 조회
            List<Word> words = wordbookWordRepository.findWordsByWordbookId(wordbookId);

            return words;
        });
    }

    // 단어장에 있는 단어 수정
    public CompletableFuture<List<Word>> modifyWordsInWordbook(Long wordbookId, List<WordUpdateRequest> wordUpdateRequestList) {

        return CompletableFuture.supplyAsync(() -> {
            // 1. 수정 대상 단어장 조회
            Wordbook wordbook = wordbookRepository.findById(wordbookId).orElseThrow(WordbookNotFound::new);

            List<Word> modifiedWords = wordUpdateRequestList.stream().map(request -> {
                // 2. 수정 대상 단어 조회
                Long wordId = request.getWordId();
                Word word = wordRepository.findById(wordId).orElseThrow(WordNotFound::new);

                // 3. 해당 단어가 단어장에 있는 지 조회
                if(!wordbookWordRepository.existsByWordbookAndWord(wordbook, word)){
                    throw new WordNotExistInWordbook();
                }

                // 4. 단어 수정
                word.update(request);

                return word;
            }).collect(Collectors.toList());

            wordRepository.saveAll(modifiedWords);
            return modifiedWords;
        });

    }

    // 단어장에 있는 단어 삭제
    public CompletableFuture<List<Word>> deleteWordsInWordbook(Long wordbookId, List<WordDeleteRequest> wordDeleteRequestList) {

        return CompletableFuture.supplyAsync(() -> {
            // 1. 단어 삭제 대상 단어장 조회
            Wordbook wordbook = wordbookRepository.findById(wordbookId).orElseThrow(WordbookNotFound::new);

            List<Word> deletedWords = wordDeleteRequestList.stream().map(request -> {
                Long wordId = request.getWordId();
                Word word = wordRepository.findById(wordId).orElseThrow(WordNotFound::new);
                WordbookWord wordbookWord = wordbookWordRepository.findByWord(word);

                // 2. 연관관계 해제 및 중계 테이블(WordbookWord) row 삭제
                word.removeWordbookWord(wordbookWord);

                // 3. 단어 삭제
                wordRepository.deleteById(wordId);
                return word;
            }).toList();

            return deletedWords;
        });
    }
}
