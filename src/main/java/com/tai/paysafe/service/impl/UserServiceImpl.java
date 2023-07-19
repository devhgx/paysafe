package com.tai.paysafe.service.impl;

import com.tai.paysafe.dto.response.RegistrationRequest;
import com.tai.paysafe.entities.User;
import com.tai.paysafe.entities.UserBank;
import com.tai.paysafe.errors.exception.ErrorException;
import com.tai.paysafe.repository.BankRepository;
import com.tai.paysafe.repository.UserBankRepository;
import com.tai.paysafe.repository.UserRepository;
import com.tai.paysafe.service.UserService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserBankRepository userBankRepository;
    @Autowired
    private BankRepository bankRepository;

    @Autowired
    @Lazy
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    @Transactional
    public User create(RegistrationRequest request, String role) {
        var user = userRepository.save(new User(
                0L,
                request.getUsername(),
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                role,
                request.getPhoneNumber(),
                bCryptPasswordEncoder.encode(request.getPassword()),
                null,
                new Date(),
                null
        ));
        var bank = bankRepository.findById(request.getBankId()).orElseThrow(() -> new NotFoundException("not found bank id = " + request.getBankId()));
        var userBank = new UserBank();
        userBank.setBank(bank);
        userBank.setAccountBankNumber(request.getAccountBankNumber());
        userBank.setAccountName(request.getAccountBankName());
        userBank.setBalance(BigDecimal.ZERO);
        userBank.setCreatedDate(new Date());
        userBank.setUser(user);
        userBankRepository.save(userBank);
        return user;

    }

    @Override
    @Transactional
    public void deleteUserBank(Long userId) throws ErrorException {
        var user = userRepository.findById(userId).get();
        var userBanks = user.getUserBanks();
        if (userBanks == null) {
            log.error("not found book bank");
            throw new ErrorException("not found book bank");
        }
        user.setUserBanks(null);
        userBankRepository.deleteAll(userBanks);
    }

    @Override
    public User findById(Long id) {
        var user = userRepository.findById(id).orElse(null);
        if (user != null) {
            var userNank = userBankRepository.findByUserId(user.getId()).orElse(List.of());
            user.setUserBanks(new HashSet<>(userNank));
        }
        return user;
    }

    @Override
    public List<User> getAllUser() {
        Set<User> users = userRepository.getAllUser().orElse( new HashSet<>());
        return users.stream().toList();
    }

    @Override
    public List<UserBank> getUserBanks(Long userId) {
        return userBankRepository.findByUserId(userId).orElse(null);
    }


}
