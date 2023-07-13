package com.tai.paysafe.repository;

import com.tai.paysafe.entities.Confirmation;
import com.tai.paysafe.entities.RefreshToken;
import com.tai.paysafe.entities.Transaction;
import com.tai.paysafe.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfirmationRepository extends JpaRepository<Confirmation,Long> {
    Optional<Confirmation> findByUserAndTransaction(User user, Transaction transaction);
}