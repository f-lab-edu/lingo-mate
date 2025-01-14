package org.example.domain.member;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.member.entity.Member;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Repository
public class MemberRepository {

    @PersistenceContext
    private EntityManager em;

    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    public Member findById(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        List<Member> members = em.createQuery("select m from Member m", Member.class).getResultList();
        return members;
    }
    public Optional<Member> findByEmail(String email) {
        Member member = em.createQuery("select m from Member m where m.email = :email", Member.class)
                .setParameter("email", email).getSingleResult();
        return Optional.ofNullable(member);
    }

    public Optional<Member> findByEmailAndPassword(String email, String password) {
        Optional<Member> member = findAll().stream().filter(m -> m.getEmail().equals(email)).findFirst();

        if(member.isPresent()) {
            if(password.equals(member.get().getPassword())) {
                return member;
            }
        }

        return Optional.empty();
    }
}
