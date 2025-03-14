package org.example.domain.wordbook;

import org.example.domain.wordbook.entity.Word;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WordRepository extends JpaRepository<Word, Long> {
}
