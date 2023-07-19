package com.tai.paysafe.controller;

import com.tai.paysafe.constants.ResponseStatusMessage;
import com.tai.paysafe.dto.request.BankRequest;
import com.tai.paysafe.dto.response.ResponseData;
import com.tai.paysafe.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/banks")
public class BankController {

    @Autowired

    private BankService bankService;

    @GetMapping("/list")
    @PreAuthorize("hasRole('ROLE_USER') || hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseData> getBank() {
        return ResponseEntity.ok(new ResponseData(HttpStatus.OK.value(), ResponseStatusMessage.SUCCESS, bankService.getBanks()));
    }


}
