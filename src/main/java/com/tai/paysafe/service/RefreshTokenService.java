package com.tai.paysafe.service;

import com.tai.paysafe.entities.RefreshToken;
import com.tai.paysafe.entities.User;
import com.tai.paysafe.errors.exception.TokenRefreshException;
import com.tai.paysafe.repository.RefreshTokenRepository;
import com.tai.paysafe.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class RefreshTokenService {

    @Value("${jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public int deleteRefreshToken(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent()) {
            throw new NotFoundException("user not found");
        }
        int data = refreshTokenRepository.deleteByUserId(userId);
        return data;
    }

    public RefreshToken createRefreshToken(Long userId) {
        User user = userRepository.findById(userId).get();

        Optional<RefreshToken> oldRefreshToken = refreshTokenRepository.findByUser(user);
//        if(oldRefreshToken.isPresent()){
//            refreshTokenRepository.deleteByUserId(user.getId());
//        }

        RefreshToken refreshToken = new RefreshToken();
        if (oldRefreshToken.isPresent()) {
            refreshToken.setId(oldRefreshToken.get().getId());
        }
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
        }

        return token;
    }
}