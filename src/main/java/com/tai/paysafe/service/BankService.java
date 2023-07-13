package com.tai.paysafe.service;

import com.tai.paysafe.dto.request.BankRequest;
import com.tai.paysafe.entities.Bank;

import java.util.List;

public interface BankService {
    Bank createBank(BankRequest bank);

    Bank getBankById(Long id);

    Bank updateBank(Long id, BankRequest bank);

    void deleteBank(Long id);

    List<Bank> getBanks();
}
