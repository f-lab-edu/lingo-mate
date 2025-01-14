package org.example.domain.member;

import jakarta.persistence.EntityManager;
import org.example.domain.member.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback
class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    EntityManager em;
    // 테스타가 실했다가 성공했다가 반복 -> 테스트에 사용하는 데이터가 같아서 중복된 데이터가 삽입될 가능성이 있음.
    // 각 테스트가 독립적으로 실행되도록 해야 함 -> 테스트 데이터를 다르게 구성?
    @Test
    void findByEmailTest() {
        //Given
        Member member = Member.createMember(MemberTestFixture.createMemberJoinRequest());
        memberRepository.save(member);
        
        //When
        Member findMember = memberRepository.findByEmail("valid@example.com").orElseThrow();
        //Then
        // 통과 실패 Assertions.assertThat(findMember).isEqualTo(member); 
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