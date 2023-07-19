package com.tai.paysafe.service.impl;

import com.tai.paysafe.entities.UserStatement;
import com.tai.paysafe.errors.exception.BadRequstException;
import com.tai.paysafe.errors.exception.ErrorException;
import com.tai.paysafe.repository.UserRepository;
import com.tai.paysafe.repository.UserStatementRepository;
import com.tai.paysafe.service.UserStatementService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserStatementServiceImpl implements UserStatementService {
    @Autowired
    private UserStatementRepository userStatementRepository;
    @Autowired
    private UserRepository userRepository;

    @Transactional
    @Override
    public UserStatement createStatement(UserStatement userStatement, Long userId) {
        var user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new NotFoundException("not found user");
        }
        var current = userStatementRepository.findByUserAndActiveStatus(userId, true).orElse(null);
        if (current == null) {
            throw new NotFoundException("not found user statement");
        }
        current.setActiveStatus(false);
        current.setUpdatedDate(new Date());
        userStatementRepository.save(current);
        return userStatementRepository.save(userStatement);
    }

    @Override
    public Optional<UserStatement> getStatementById(Long id) {
        return userStatementRepository.findById(id);
    }

    @Override
    public Page<UserStatement> getStatementAllStatusByUser(int pageNumber, int pageSize, Long userId) {
        if (pageNumber < 0 || pageSize < 0) {
            log.error("limit or page = 0");
            throw new BadRequstException("limit or page = 0");
        }
        Sort sort = Sort.by("id").descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        return userStatementRepository.findAllByUser(userId, pageable);
    }

    @Override
    public UserStatement getStatementByUserId(Long userId) {
        var user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new NotFoundException("not found user");
        }
        return userStatementRepository.findByUserAndActiveStatus(userId, true).orElse(null);
    }

    @Override
    public List<UserStatement> getAllStatements() {
        return userStatementRepository.findAll();
    }

    @Override
    public UserStatement updateStatement(UserStatement userStatement) {
        return userStatementRepository.save(userStatement);
    }

    @Override
    public void deleteStatement(Long id) {
        userStatementRepository.deleteById(id);
    }
}
