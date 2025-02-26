package org.example.domain.wordbook;

import org.example.domain.wordbook.entity.Wordbook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WordbookRepository extends JpaRepository<Wordbook, Long> {


}
