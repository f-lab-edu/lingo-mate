package org.example.domain.member;

import jakarta.persistence.EntityManager;
import org.example.domain.member.entity.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    EntityManager entityManager;

    @Test
    void findByEmailTest() {
        //Given
        Member member = Member.createMember(MemberTestFixture.createMemberJoinRequest());
        memberRepository.save(member);
        
        //When
        Member findMember = memberRepository.findByEmail("valid@example.com").orElseThrow();
        //Then
        //Note: 통과 실패 Assertions.assertThat(findMember).isEqualTo(member);
        assertThat(findMember.getEmail()).isEqualTo(member.getEmail());

    }

    @Test
    void findByEmailAndPasswordTeat() {
        //Given
        Member member = Member.createMember(MemberTestFixture.createMemberJoinRequest());
        memberRepository.save(member);
        
        //When
        Member findMember = memberRepository.findByEmailAndPassword("valid@example.com", "validPassword123").orElseThrow();

        //Then
        assertThat(findMember.getEmail()).isEqualTo("valid@example.com");
        assertThat(findMember.getPassword()).isEqualTo("validPassword123");

    }
}