package org.example.domain.wordbook;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class WordbookServiceTest {
    @InjectMocks
    WordbookService wordbookService;
    @Mock
    WordbookRepository wordbookRepository;

    @Test
    void 단어장_생성을_확인한다() {
        //Given


    }


}
