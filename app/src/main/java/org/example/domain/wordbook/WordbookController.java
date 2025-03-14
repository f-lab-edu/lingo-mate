package org.example.domain.wordbook;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.domain.auth.annotation.LoginMember;
import org.example.domain.wordbook.dto.request.WordCreateRequest;
import org.example.domain.wordbook.dto.request.WordDeleteRequest;
import org.example.domain.wordbook.dto.request.WordUpdateRequest;
import org.example.domain.wordbook.dto.request.WordbookCreateRequest;
import org.example.domain.wordbook.dto.response.*;
import org.example.domain.wordbook.entity.Word;
import org.example.domain.wordbook.entity.Wordbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wordbook")
public class WordbookController {
    private final WordbookService wordbookService;

    // 단어장 생성
    @PostMapping("/create")
    public CompletableFuture<ResponseEntity<WordbookResponse>> wordbookCreate(@LoginMember Long memberId, @Valid @RequestBody WordbookCreateRequest wordbookCreateRequest) {
        return wordbookService.createWordbook(memberId, wordbookCreateRequest).thenApply(wordbook -> {
            WordbookResponse response = WordbookResponse.create(wordbook);
            return ResponseEntity.ok().body(response);
        });
    }

    // 단어장 삭제
    @DeleteMapping("/{wordbook_id}/delete")
    public CompletableFuture<ResponseEntity<WordbookResponse>> wordbookDelete(@LoginMember Long memberId, @PathVariable("wordbook_id") Long wordbookId) {
        return wordbookService.deleteWordbook(memberId, wordbookId).thenApply(wordbook -> {
            WordbookResponse response = WordbookResponse.create(wordbook);
            return ResponseEntity.ok().body(response);
        });
    }

    // 단어장 목록 조회
    @GetMapping("/all")
    public CompletableFuture<ResponseEntity<Page<WordbookResponse>>> wordbookList(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return wordbookService.findAllWordbook(pageable).thenApply(wordbookResponses -> {
            return ResponseEntity.ok().body(wordbookResponses);
        });
    }

    // 단어장에 단어 추가
    @PostMapping("/{wordbook_id}")
    public CompletableFuture<ResponseEntity<List<WordCreateResponse>>> addWord(@LoginMember Long memberId,
                                                                               @PathVariable("wordbook_id") Long wordbookId,
                                                                               @Valid @RequestBody List<WordCreateRequest> wordCreateRequest) {
        return wordbookService.createAndAddWord(wordbookId, wordCreateRequest).thenApply(words -> {
            List<WordCreateResponse> response = words.stream().map(WordCreateResponse::create).toList();
            return ResponseEntity.ok().body(response);
        });
    }

    // 단어장 내용 조회
    @GetMapping("/{wordbook_id}/words")
    public CompletableFuture<ResponseEntity<List<WordsInWordbookResponse>>> wordbookDetails(@LoginMember Long memberId, @PathVariable("wordbook_id") Long wordbook_id) {
        return wordbookService.getWordsInWordbook(wordbook_id).thenApply(words -> {
            List<WordsInWordbookResponse> response = words.stream().map(WordsInWordbookResponse::create).collect(Collectors.toList());
            return ResponseEntity.ok().body(response);
        });
    }


    // 단어장에 있는 단어 수정
    @PutMapping("/{wordbook_id}/words")
    public CompletableFuture<ResponseEntity<List<WordUpdateResponse>>> modifyWords(@LoginMember Long memberId, @PathVariable("wordbook_id") Long wordbook_id, @RequestBody List<WordUpdateRequest> wordUpdateRequestList) {
        return wordbookService.modifyWordsInWordbook(wordbook_id, wordUpdateRequestList).thenApply(modifiedWords -> {
            List<WordUpdateResponse> response = modifiedWords.stream().map(WordUpdateResponse::create).collect(Collectors.toList());
            return ResponseEntity.ok().body(response);
        });
    }

    // 단어장 단어 삭제
    @DeleteMapping("/{wordbook_id}/words")
    public CompletableFuture<ResponseEntity<List<WordDeleteResponse>>> deleteWords(@LoginMember Long memberId, @PathVariable("wordbook_id") Long wordbook_id, @RequestBody List<WordDeleteRequest> wordDeleteRequestList) {
        return wordbookService.deleteWordsInWordbook(wordbook_id, wordDeleteRequestList).thenApply(deletedWords -> {
            List<WordDeleteResponse> response = deletedWords.stream().map(WordDeleteResponse::create).collect(Collectors.toList());
            return ResponseEntity.ok().body(response);
        });
    }
}
