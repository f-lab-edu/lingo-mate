package org.example.domain.auth;

import org.example.domain.auth.entity.AuthEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthRepository extends JpaRepository<AuthEntity, Long> {

    Optional<AuthEntity> findByRefreshToken(String refreshToken);
    Optional<AuthEntity> findByAccessToken(String accessToken);
    @Query("DELETE FROM AuthEntity a WHERE a.id = :authId")
    @Modifying
    void deleteByAuthId(@Param("authId") Long authId);

    @Query("DELETE FROM AuthEntity a WHERE a.id = :memberId")
    @Modifying
    void deleteByMemberId(@Param("memberId") Long memberId);

    boolean existsByMemberId(Long memberId);

    @Modifying
    void deleteByRefreshToken(String refreshToken);
}
