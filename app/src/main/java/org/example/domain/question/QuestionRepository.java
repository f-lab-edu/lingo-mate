package org.example.domain.question;

import org.example.domain.member.entity.Member;
import org.example.domain.question.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query("SELECT q FROM Question q WHERE q.title LIKE %:keyword% OR q.content LIKE %:keyword%")
    List<Question> findByKeyword(@Param("keyword") String keyword);

    @Query("SELECT q FROM Question q WHERE q.member.id = :memberId")
    List<Question> findByMemberId(@Param("memberId") Long memberId);

    @Query("SELECT m FROM Member m WHERE m.username = :username")
    Optional<Member> findMemberByUsername(@Param("username") String username);
}
