package com.tai.paysafe.controller;

import com.tai.paysafe.constants.RoleType;
import com.tai.paysafe.constants.TransactionProcessStatus;
import com.tai.paysafe.dto.response.ResponseData;
import com.tai.paysafe.service.TransactionService;
import com.tai.paysafe.utils.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private TransactionService transactionService;
    //deposit
    //withdraw
    //list transaction
    @GetMapping("/list/{pageNumber}/{pageSize}")
    @PreAuthorize("hasRole('ROLE_USER') || hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseData> transactionsList(@Valid @PathVariable("pageNumber") int pageNumber ,@Valid @PathVariable("pageSize") int pageSize) throws Exception {
        var jwt = jwtUtil.getClaims();
        String role = jwt.getRole();
        Long userId = jwt.getUserId();
        int processStatus = TransactionProcessStatus.ADMIN_APPROVE;
        if (role.equals(RoleType.ADMIN)) {
            processStatus = TransactionProcessStatus.USER_APPROVE;
        }
        var data = transactionService.getTransactions( pageNumber, pageSize, processStatus, userId);
        return ResponseEntity.ok(new ResponseData(HttpStatus.OK.value(), "success", data));
    }

    @GetMapping("/listAll/{pageNumber}/{pageSize}")
    @PreAuthorize("hasRole('ROLE_USER') || hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseData> transactionsListAll(@Valid @PathVariable("pageNumber") int pageNumber ,@Valid @PathVariable("pageSize") int pageSize) throws Exception {
        var jwt = jwtUtil.getClaims();
        String role = jwt.getRole();
        Long userId = jwt.getUserId();
        var data = transactionService.getTransactionsAll( pageNumber, pageSize, role, userId);
        return ResponseEntity.ok(new ResponseData(HttpStatus.OK.value(), "success", data));
    }
}
