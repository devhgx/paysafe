package com.tai.paysafe.repository;

import com.tai.paysafe.entities.Bank;
import com.tai.paysafe.entities.UserStatement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankRepository extends JpaRepository<Bank, Long> {
}