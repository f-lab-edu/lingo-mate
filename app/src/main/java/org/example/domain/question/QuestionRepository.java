package org.example.domain.question;

import org.example.domain.member.entity.Member;
import org.example.domain.question.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    Page<Question> findByMemberId(Pageable pageable, @Param("memberId") Long memberId);
    Optional<Member> findMemberById(@Param("id") Long id);

    @Query("SELECT q FROM Question q LEFT JOIN FETCH q.comments WHERE q.id = :questionId")
    Optional<Question> findByIdWithComments(@Param("questionId") Long questionId);
}
