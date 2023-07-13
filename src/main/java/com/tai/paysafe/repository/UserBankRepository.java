package com.tai.paysafe.repository;

import com.tai.paysafe.entities.User;
import com.tai.paysafe.entities.UserBank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserBankRepository extends JpaRepository<UserBank, Long> {
    Optional<List<UserBank>> findByUserId(Long userId);
}