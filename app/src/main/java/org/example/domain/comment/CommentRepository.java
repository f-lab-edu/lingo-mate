package org.example.domain.comment;

import org.example.domain.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {

    @Query("SELECT c FROM Comment c LEFT JOIN FETCH c.question WHERE c.question.id = :questionId")
    List<Comment> findByQuestionId(@Param("questionId") Long questionId);
}
