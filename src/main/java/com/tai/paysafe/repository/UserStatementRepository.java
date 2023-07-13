package com.tai.paysafe.repository;
import com.tai.paysafe.entities.UserStatement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserStatementRepository extends JpaRepository<UserStatement, Long>, JpaSpecificationExecutor<UserStatement> {
    @Query(value = "select * from user_statement where user_id = :userId and active_status = :activeStatus", nativeQuery = true)
    Optional<UserStatement> findByUserAndActiveStatus(Long userId, boolean activeStatus);
    @Query(value = "select * from user_statement where user_id = :userId", nativeQuery = true)
    Page<UserStatement> findAllByUser(Long userId, Pageable pageable );
}