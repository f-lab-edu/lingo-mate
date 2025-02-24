package org.example.domain.wordbook;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.domain.auth.annotation.LoginMember;
import org.example.domain.wordbook.dto.request.WordCreateRequest;
import org.example.domain.wordbook.dto.request.WordbookCreateRequest;
import org.example.domain.wordbook.dto.response.WordResponse;
import org.example.domain.wordbook.dto.response.WordbookResponse;
import org.example.domain.wordbook.entity.Word;
import org.example.domain.wordbook.entity.Wordbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/wordbook")
public class WordbookController {
    private final WordbookService wordbookService;

    // 단어장 생성
    @PostMapping("/create")
    public ResponseEntity<WordbookResponse> wordbookCreate(@LoginMember Long memberId, @Valid @RequestBody WordbookCreateRequest wordbookCreateRequest){
        Wordbook wordbook = wordbookService.createWordbook(memberId, wordbookCreateRequest);
        WordbookResponse response = WordbookResponse.create(wordbook);
        return ResponseEntity.ok().body(response);
    }

    // 단어장 목록 조회
    @GetMapping("/all")
    public ResponseEntity<Page<WordbookResponse>> wordbookList(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        Page<WordbookResponse> response = wordbookService.findAllWordbook(pageable);
        return ResponseEntity.ok().body(response);
    }

    // 단어장에 단어 추가
    @PostMapping("/{wordbook_id}")
    public ResponseEntity<WordResponse> addWord(@LoginMember Long memberId,
                                         @PathVariable("wordbook_id") Long wordbookId,
                                         @Valid @RequestBody WordCreateRequest wordCreateRequest) {
        Word word = wordbookService.createAndAddWord(wordbookId, wordCreateRequest);
        WordResponse response = WordResponse.create(word);
        return ResponseEntity.ok().body(response);
    }

    // 단어장 내용 조회
    @GetMapping("/{wordbook_id}")
    public void wordbookDetails() {
        
    }


    // 단어장 단어 수정
    @PutMapping("/{wordbook_id}")
    public void modifyWords() {

    }

    // 단어장 단어 삭제
    @DeleteMapping("/{wordbook_id}")
    public void deleteWords() {

    }
}
