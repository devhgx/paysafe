package com.tai.paysafe.repository;

import com.tai.paysafe.entities.RefreshToken;
import com.tai.paysafe.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByUser(User user);

//    @Modifying
    int deleteByUserId(Long userId);
}