package com.tai.paysafe.service.impl;

import com.tai.paysafe.dto.request.BankRequest;
import com.tai.paysafe.entities.Bank;
import com.tai.paysafe.repository.BankRepository;
import com.tai.paysafe.service.BankService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class BankServiceImpl implements BankService {
    @Autowired
    private BankRepository bankRepository;

    public Bank createBank(BankRequest bank) {
        return bankRepository.save(new Bank(0L,bank.getCode(),bank.getBankName(), new Date(), null));
    }

    public Bank getBankById(Long id) {
        return bankRepository.findById(id).orElseThrow(()-> new NotFoundException("not found bank id = "+id));
    }

    public Bank updateBank(Long id, BankRequest bank) {
        Bank existingBank = bankRepository.findById(id).orElseThrow(()-> new NotFoundException("not found bank id = "+id));
        if (existingBank != null) {
            existingBank.setCode(bank.getCode());
            existingBank.setBankName(bank.getBankName());
            return bankRepository.save(existingBank);
        }
        return null;
    }

    public void deleteBank(Long id) {
        bankRepository.deleteById(id);
    }

    @Override
    public List<Bank> getBanks() {
        return bankRepository.findAll();
    }
}
