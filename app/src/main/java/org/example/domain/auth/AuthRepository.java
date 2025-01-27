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
    boolean findByAccessToken(String accessToken);
    @Query("DELETE FROM AuthEntity a WHERE a.id = :autId")
    @Modifying
    void deleteByAuthId(@Param("auth_id") Long authId);

    @Modifying
    void deleteByRefreshToken(String refreshToken);
}
