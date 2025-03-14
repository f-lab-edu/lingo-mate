package org.example.domain.wordbook;

import org.assertj.core.api.Assertions;
import org.example.domain.member.MemberRepository;
import org.example.domain.member.MemberTestFixture;
import org.example.domain.member.entity.Member;
import org.example.domain.wordbook.dto.request.WordbookCreateRequest;
import org.example.domain.wordbook.entity.Wordbook;
import org.example.domain.wordbook.entity.WordbookWord;
import org.example.domain.wordbook.fixture.WordbookTestFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WordbookServiceTest {
    @InjectMocks
    WordbookService wordbookService;
    @Mock
    WordbookRepository wordbookRepository;
    @Mock
    WordbookWordRepository wordbookWordRepository;
    @Mock
    WordRepository wordRepository;
    @Mock
    MemberRepository memberRepository;

    @Test
    void 단어장_생성을_확인한다() throws ExecutionException, InterruptedException {
        //Given
        Member member = MemberTestFixture.createMember();
        Long memberId = member.getId();
        WordbookCreateRequest wordbookCreateRequest = WordbookTestFixture.createWordbookCreateRequest();
        Wordbook wordbook = WordbookTestFixture.createWordbook();
        member.addWordBook(wordbook); // 연관관계 설정

        //when
        when(memberRepository.findByIdWithWordbooks(anyLong())).thenReturn(Optional.of(member));
        when(wordbookRepository.save(any())).thenReturn(wordbook);

        //Then
        Wordbook createdWordbook = wordbookService.createWordbook(memberId, wordbookCreateRequest).get();
        Assertions.assertThat(createdWordbook.getMember()).isEqualTo(member);
        Assertions.assertThat(createdWordbook.getDescription()).isEqualTo("description");
        Assertions.assertThat(createdWordbook.getTitle()).isEqualTo("title");

    }

    @Test
    void 단어장_삭제를_확인한다() {
        /*Given
        Member member = MemberTestFixture.createMember();
        Long memberId = member.getId();
        Wordbook wordbook = WordbookTestFixture.createWordbook();
        Long wordbookId = wordbook.getId();
        member.addWordBook(wordbook); // 연관관계 설정

        /when
        when(wordbookRepository.findById(anyLong())).thenReturn(Optional.of(wordbook));
        when(wordbookWordRepository.findAllByWordbook()).thenReturn()
        when(wordRepository.findById())
        when(wordRepository.deleteAll())
        when(wordbookRepository.delete())

        */


    }

    @Test
    void 단어장에_있는_단어들을_확인한다() {

    }

}
