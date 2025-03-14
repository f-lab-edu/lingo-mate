package org.example.domain.member;

import org.example.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    // save()
    // findById()
    // findAll()
    Optional<Member> findByEmail(String email);
    Optional<Member> findByUsername(String username);
    Long findIdByUsername(String username);
    Boolean existsByEmail(String email);
    Optional<Member> findByEmailAndPassword(String email, String password);

    @Query("SELECT m FROM Member m LEFT JOIN FETCH m.wordbooks WHERE m.id = :memberId")
    Optional<Member> findByIdWithWordbooks(@Param("memberId") Long memberId);

    @Query("SELECT m FROM Member m LEFT JOIN FETCH m.questions WHERE m.id = :memberId")
    Optional<Member> findByIdWithQuestions(@Param("memberId") Long memberId);

    @Query("SELECT m FROM Member m LEFT JOIN FETCH m.comments WHERE m.id = :memberId")
    Optional<Member> findByIdWithComments(@Param("memberId") Long memberId);
}
