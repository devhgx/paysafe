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
    Page<Transaction> findAllActiveByAdmin(int processStatus, Pageable pageable);
    @Query(value = "select t.* from  transaction  t  where t.process_status = :processStatus and confirmation_status = false and recipient_account_id = :recipientAccountId", nativeQuery = true)
    Page<Transaction> findAllActiveByUser(int processStatus, Long recipientAccountId, Pageable pageable);

    @Query(value = "select t.* from transaction t  where " +
            "        t.recipient_account_id = :userId or " +
            "        t.sender_account_id = :userId or " +
            "        t.user_bank_id = (select ub.id  from user_bank ub where ub.user_id = :userId) order by t.id desc", nativeQuery = true)
    Page<Transaction> findAllStatusByUser( Long userId, Pageable pageable);

    @Query(value = "select t.* from transaction t  order by t.id desc", nativeQuery = true)
    Page<Transaction> findAllStatusByAdmin( Long userId, Pageable pageable);
}