package org.example.domain.member;


import lombok.extern.slf4j.Slf4j;
import org.example.domain.member.entity.Member;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Repository
public class MemberRepository {
    private ConcurrentHashMap<Long, Member> store = new ConcurrentHashMap<>();

    public Member save(Member member) {
        log.debug("save: memberId={}", member.getId());
        store.put(member.getId(), member);
        return member;
    }

    public Member findById(Long id) {
        return store.get(id);
    }

    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }
    public Optional<Member> findByEmail(String email) {
        return findAll().stream().filter(m -> m.getEmail().equals(email)).findFirst();
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

    public void clearStore() {
        store.clear();
    }
}
