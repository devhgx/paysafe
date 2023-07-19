package com.tai.paysafe.service.impl;

import com.tai.paysafe.constants.RoleType;
import com.tai.paysafe.constants.TransactionProcessStatus;
import com.tai.paysafe.constants.TransactionType;
import com.tai.paysafe.dto.request.DepositRequest;
import com.tai.paysafe.dto.request.TransferRequest;
import com.tai.paysafe.dto.request.WithdrawRequest;
import com.tai.paysafe.entities.Confirmation;
import com.tai.paysafe.entities.Transaction;
import com.tai.paysafe.entities.UserStatement;
import com.tai.paysafe.errors.exception.ApproveException;
import com.tai.paysafe.errors.exception.BadRequstException;
import com.tai.paysafe.errors.exception.WithdrawException;
import com.tai.paysafe.repository.*;
import com.tai.paysafe.service.TransactionService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.math.BigDecimal;
import java.util.Date;

@Slf4j
@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BankRepository bankRepository;
    @Autowired
    private UserBankRepository userBankRepository;
    @Autowired
    private UserStatementRepository userStatementRepository;
    @Autowired
    private ConfirmationRepository confirmationRepository;

    private UserStatement updateBalance(BigDecimal amount, Long userId, String transactionType) {
        log.info("------------ updateBalance user id = " + userId);
        var exist = userStatementRepository.findByUserAndActiveStatus(userId, true).orElse(null);
        BigDecimal balance = BigDecimal.ZERO;
        if (exist != null) {
            exist.setUpdatedDate(new Date());
            exist.setActiveStatus(false);
            if (transactionType.equals(TransactionType.DEPOSIT)) {
                balance = exist.getBalance().add(amount);
            } else if (transactionType.equals(TransactionType.WITHDRAW)) {
                balance = exist.getBalance().subtract(amount);
                if (balance.compareTo(BigDecimal.ZERO) < 0) {
                    var errorText = "cannot withdraw , balance less than " + amount;
                    log.error(errorText);
                    throw new WithdrawException(errorText);
                }

            }
            userStatementRepository.save(exist);
            return userStatementRepository.save(new UserStatement(0L, balance, exist.getIncome(), exist.getExpense(), true, userId, new Date(), null));
        }
        if (transactionType.equals(TransactionType.WITHDRAW)) {
            log.error("cannot withdraw " + amount);
            throw new WithdrawException("cannot withdraw " + amount);
        }
        return userStatementRepository.save(new UserStatement(0L, amount.compareTo(BigDecimal.ZERO) >= 0 ? amount : BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, true, userId, new Date(), null));
    }

    private UserStatement updateBalanceTransferLastApprove(BigDecimal amount, Long fromUserId, Long toUserId) {
        log.info("------------ updateBalanceTransferLastApprove fromUserId id = " + fromUserId + "toUserId id = " + toUserId);
        var existFromUserId = userStatementRepository.findByUserAndActiveStatus(fromUserId, true).orElse(null);
        var existToUserId = userStatementRepository.findByUserAndActiveStatus(toUserId, true).orElse(null);
        if (existFromUserId != null) {
            existFromUserId.setActiveStatus(false);
            existFromUserId.setUpdatedDate(new Date());
            userStatementRepository.save(existFromUserId);
        }
        if (existToUserId != null) {
            existToUserId.setActiveStatus(false);
            existToUserId.setUpdatedDate(new Date());
            userStatementRepository.save(existToUserId);
        }

        // From
        var fromBalance = existFromUserId.getBalance().subtract(amount);
        if (fromBalance.compareTo(BigDecimal.ZERO) < 0) {
            log.error("cannot withdraw , balance less than " + amount);
            throw new WithdrawException("cannot withdraw , balance less than " + amount);
        }
        userStatementRepository.save(new UserStatement(0L, fromBalance, existFromUserId.getIncome(), existFromUserId.getExpense().add(amount), true, fromUserId, new Date(), null));
        // To
        var toBalance = amount;
        var income = amount;
        var expense= BigDecimal.ZERO;
        if (existToUserId != null) {
            toBalance = existToUserId.getBalance().add(amount);
            income = existToUserId.getIncome().add(amount);
            expense = existToUserId.getExpense();
        }
        userStatementRepository.save(new UserStatement(0L, toBalance, income,expense, true, toUserId, new Date(), null));

        return null;
    }

    @Transactional
    @Override
    public Transaction deposit(DepositRequest depositRequest, Long userId) {
        var recipientAccount = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Cannot found user " + userId));
        var depositBank = bankRepository.findById(depositRequest.getDepositFromBankId()).orElseThrow(() -> new NotFoundException("Cannot found bank " + depositRequest.getDepositFromBankId()));

        var transaction = new Transaction(0L, TransactionType.DEPOSIT, depositRequest.getAmount(), true, TransactionProcessStatus.SUCCESS, null, depositBank, depositRequest.getDepositFromAccountBankNumber(), depositRequest.getDepositFromAccountName(), null, recipientAccount, depositRequest.getNote(), new Date(), null

        );
        updateBalance(depositRequest.getAmount(), userId, TransactionType.DEPOSIT);
        return transactionRepository.save(transaction);
    }

    @Transactional
    @Override
    public Transaction withdraw(WithdrawRequest withdrawRequest, Long userId) {
        var withdrawUserBank = userBankRepository.findById(withdrawRequest.getUserBankId()).orElseThrow(() -> new NotFoundException("Cannot found user bank " + withdrawRequest.getUserBankId()));
        updateBalance(withdrawRequest.getAmount(), userId, TransactionType.WITHDRAW);
        var transaction = new Transaction(0L, TransactionType.WITHDRAW, withdrawRequest.getAmount(), true, TransactionProcessStatus.SUCCESS, withdrawUserBank, null, null, null, null, null, withdrawRequest.getNote(), new Date(), null

        );

        return transactionRepository.save(transaction);
    }

    @Override
    public Transaction transfer(TransferRequest transferRequest, Long userId) {
        var senderUser = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("not found sender user " + userId));
        var recipientUser = userRepository.findByUsername(transferRequest.getRecipientUserName()).orElseThrow(() -> new NotFoundException("not found recipient  user " + userId));
        var adminUser = userRepository.findByRole(RoleType.ADMIN).orElseThrow(() -> new NotFoundException("not found recipient  user " + userId));
        var transaction = new Transaction(0L, TransactionType.TRANSFER, transferRequest.getAmount(), false, TransactionProcessStatus.ADMIN_APPROVE, null, null, null, null, senderUser, recipientUser, transferRequest.getNote(), new Date(), null);
        transaction = transactionRepository.save(transaction);
        //recipientConfirm

        //adminConfirm
        confirmationRepository.save(new Confirmation(0L, transaction, adminUser, false, new Date(), null));
        return transactionRepository.findById(transaction.getId()).orElse(null);
    }

    @Transactional
    @Override
    public Transaction approve(Long id, Long userId, String role) {
        var transaction = transactionRepository.findById(id).orElseThrow(() -> new NotFoundException("not found transaction "));
        if (!transaction.getTransactionType().equals(TransactionType.TRANSFER)) {
            var errorText = String.format("cannot approve TransactionType: %s , id =  ", transaction.getTransactionType(), transaction.getId());
            log.error(errorText);
            throw new ApproveException(errorText);
        }

        if (role.equals(RoleType.ADMIN) && transaction.getProcessStatus() == TransactionProcessStatus.ADMIN_APPROVE) {
            transaction.setConfirmationStatus(true);
            transaction.setProcessStatus(TransactionProcessStatus.SUCCESS);
            updateBalanceTransferLastApprove(transaction.getAmount(), transaction.getSenderAccount().getId(), transaction.getRecipientAccount().getId());
        } else if (role.equals(RoleType.USER) && transaction.getProcessStatus() == TransactionProcessStatus.USER_APPROVE) {
            transaction.setProcessStatus(TransactionProcessStatus.ADMIN_APPROVE);
        } else {
            var errorText = String.format("cannot withdraw ProcessStatus: %s , id = %d ", transaction.getProcessStatus(), transaction.getId());
            log.error(errorText);
            throw new ApproveException(errorText);
        }
        var user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("not found sender user " + userId));
        var confirm = confirmationRepository.findByUserAndTransaction(user, transaction).get();
        confirm.setConfirmationStatus(true);
        confirm.setUpdatedDate(new Date());
        confirmationRepository.save(confirm);

        transaction.setUpdatedDate(new Date());
        return transactionRepository.save(transaction);
    }

    @Override
    public Page<Transaction> getTransactions(int pageNumber, int pageSize, int processStatus, Long userId) {
        if (pageNumber < 0 || pageSize < 0) {
            var errorText = "limit or page = 0";
            log.error(errorText);
            throw new BadRequstException(errorText);
        }
        Sort sort = Sort.by("id").descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        if (processStatus == TransactionProcessStatus.USER_APPROVE) {
            return transactionRepository.findAllActiveByUser(processStatus, userId, pageable);
        }
        return transactionRepository.findAllActiveByAdmin(processStatus, pageable);
    }

    @Override
    public Page<Transaction> getTransactionsAll(int pageNumber, int pageSize, String role, Long userId) {
        if (pageNumber < 0 || pageSize < 0) {
            log.error("limit or page = 0");
            throw new BadRequstException("limit or page = 0");
        }
        Sort sort = Sort.by("id").descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        if (role.equals(RoleType.USER)) {
            return transactionRepository.findAllStatusByUser(userId, pageable);
        }
        return transactionRepository.findAllStatusByAdmin(userId, pageable);
    }

}
