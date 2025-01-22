package org.example.domain.member;

import org.example.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
