package com.tai.paysafe.service;

import com.tai.paysafe.entities.UserStatement;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface UserStatementService {
    UserStatement createStatement(UserStatement userStatement, Long userId);
    Optional<UserStatement> getStatementById(Long id);
    UserStatement getStatementByUserId(Long userId);
    List<UserStatement> getAllStatements();
    UserStatement updateStatement(UserStatement userStatement);
    Page<UserStatement> getStatementAllStatusByUser(int pageNumber, int pageSize, Long userId);
    void deleteStatement(Long id);





}
