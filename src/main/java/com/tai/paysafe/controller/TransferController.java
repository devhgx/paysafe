package com.tai.paysafe.controller;

import com.tai.paysafe.dto.request.ApproveRequest;
import com.tai.paysafe.dto.request.DepositRequest;
import com.tai.paysafe.dto.request.TransferRequest;
import com.tai.paysafe.dto.request.WithdrawRequest;
import com.tai.paysafe.dto.response.ResponseData;
import com.tai.paysafe.service.TransactionService;
import com.tai.paysafe.utils.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transfer")
public class TransferController {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/deposit")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ResponseData> deposit(@Valid @RequestBody DepositRequest depositRequest) throws Exception {
        Long userId = jwtUtil.getClaims().getUserId();
        transactionService.deposit(depositRequest, userId);
        return ResponseEntity.ok(new ResponseData(HttpStatus.OK.value(), "success", null));
    }

    @PostMapping("/withdraw")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ResponseData> withdraw(@Valid @RequestBody WithdrawRequest withdrawRequest) throws Exception {
        Long userId = jwtUtil.getClaims().getUserId();
        transactionService.withdraw(withdrawRequest, userId);
        return ResponseEntity.ok(new ResponseData(HttpStatus.OK.value(), "success", null));
    }
    @PostMapping("/toUsername")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ResponseData> transfer(@Valid @RequestBody TransferRequest transferRequest) throws Exception {
        Long userId = jwtUtil.getClaims().getUserId();
        transactionService.transfer(transferRequest, userId);
        return ResponseEntity.ok(new ResponseData(HttpStatus.OK.value(), "success", null));
    }

    @PostMapping("/approve")
    @PreAuthorize("hasRole('ROLE_USER') || hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseData> approveFromUser(@Valid @RequestBody ApproveRequest approveRequest) throws Exception {
        var jwt = jwtUtil.getClaims();
        String role = jwt.getRole();
        Long userId = jwt.getUserId();
        transactionService.approve(approveRequest.getTransactionId(), userId, role);
        return ResponseEntity.ok(new ResponseData(HttpStatus.OK.value(), "success", null));
    }


}
