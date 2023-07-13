package com.tai.paysafe.repository;

import com.tai.paysafe.entities.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Long>, JpaSpecificationExecutor<Transaction> {
    @Query(value = "select t.* from  transaction  t  where t.process_status = :processStatus and confirmation_status = false", nativeQuery = true)
    Page<Transaction> findAllByAdmin(int processStatus, Pageable pageable);
    @Query(value = "select t.* from  transaction  t  where t.process_status = :processStatus and confirmation_status = false and recipient_account_id = :recipientAccountId", nativeQuery = true)
    Page<Transaction> findAllByUser(int processStatus, Long recipientAccountId, Pageable pageable);
}