package com.tai.paysafe.repository;

import com.tai.paysafe.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends TestRepository , JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByRole(String role);
}