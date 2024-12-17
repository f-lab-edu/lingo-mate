package org.example.domain.member;


import lombok.extern.slf4j.Slf4j;
import org.example.domain.member.entity.Member;
import org.springframework.stereotype.Repository;

import java.util.*;

@Slf4j
@Repository
public class MemberRepository {
    private static Map<Long, Member> store = new HashMap<>();

    public Member save(Member member) {
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

    public void clearStore() {
        store.clear();
    }
}
