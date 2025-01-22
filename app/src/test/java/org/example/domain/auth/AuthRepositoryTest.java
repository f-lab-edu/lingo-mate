package org.example.domain.auth;

import org.example.domain.auth.entity.AuthEntity;
import org.example.domain.auth.fixture.AuthTestFixture;
import org.example.domain.member.MemberRepository;
import org.example.domain.member.MemberTestFixture;
import org.example.domain.member.entity.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AuthRepositoryTest {

    @Autowired
    private AuthRepository authRepository;

    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        Member member = MemberTestFixture.createMember();
        memberRepository.save(member);
        // org.springframework.orm.ObjectOptimisticLockingFailureException: Row was updated or deleted by another transaction (or unsaved-value mapping was incorrect): [org.example.domain.member.entity.Member#1]
        AuthEntity authEntity = AuthEntity.createWith(AuthTestFixture.createRefreshToken());
        authEntity.setMember(member);
        authRepository.save(authEntity);
    }

    @Test
    void testFindByRefreshToken() {
        String refreshToken = "mockRefreshToken";
        Optional<AuthEntity> authEntityOptional = authRepository.findByRefreshToken(refreshToken);
        assertTrue(authEntityOptional.isPresent());
    }
}