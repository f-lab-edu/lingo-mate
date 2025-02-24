package org.example.domain.wordbook;

import org.example.domain.wordbook.entity.Wordbook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WordbookRepository extends JpaRepository<Wordbook, Long> {
}
