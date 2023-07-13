package com.tai.paysafe.service;

import com.tai.paysafe.dto.request.DepositRequest;
import com.tai.paysafe.dto.request.TransferRequest;
import com.tai.paysafe.dto.request.WithdrawRequest;
import com.tai.paysafe.entities.Transaction;
import org.springframework.data.domain.Page;
public interface TransactionService {
    Transaction deposit(DepositRequest depositRequest, Long userId);
    Transaction withdraw(WithdrawRequest withdrawRequest, Long userId);
    Transaction transfer(TransferRequest transferRequest, Long userId) ;
    Transaction approve(Long id, Long userId, String role);
    Page<Transaction> getTransactions(int pageNumber, int pageSize, int processStatus, Long userId);
}
