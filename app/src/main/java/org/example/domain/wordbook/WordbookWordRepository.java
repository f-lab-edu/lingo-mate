package org.example.domain.wordbook;

import org.example.domain.wordbook.entity.Word;
import org.example.domain.wordbook.entity.Wordbook;
import org.example.domain.wordbook.entity.WordbookWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WordbookWordRepository extends JpaRepository<WordbookWord, Long> {
    @Query("SELECT w.word FROM WordbookWord w WHERE w.wordbook.id = :wordbookId")
    List<Word> findWordsByWordbookId(@Param("wordbookId") Long wordbookId);

    @Query("DELETE FROM WordbookWord w WHERE w.word = :word")
    @Modifying
    void deleteByWord(@Param("word") Word word);

    @Query("SELECT w FROM WordbookWord w WHERE w.word =  :word")
    WordbookWord findByWord(@Param("word") Word word);

    @Query("SELECT w FROM WordbookWord w WHERE w.wordbook =  :wordbook")
    List<WordbookWord> findAllByWordbook(@Param("wordbook") Wordbook wordbook);

    boolean existsByWordbookAndWord(Wordbook wordbook, Word word);
}
