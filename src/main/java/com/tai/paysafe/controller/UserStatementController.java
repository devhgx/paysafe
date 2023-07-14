package com.tai.paysafe.controller;

import com.tai.paysafe.dto.response.ResponseData;
import com.tai.paysafe.service.UserStatementService;
import com.tai.paysafe.utils.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user-statements")
public class UserStatementController {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    @Qualifier("userStatementServiceImpl")
    private UserStatementService userStatementService;


    @GetMapping("/listActive")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getStatementActiveByUser() {
        var jwt = jwtUtil.getClaims();
        Long userId = jwt.getUserId();
        return ResponseEntity.ok(new ResponseData(HttpStatus.OK.value(), "success", userStatementService.getStatementByUserId(userId)));
    }

    @GetMapping("/listAll/{pageNumber}/{pageSize}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> listAll(@Valid @PathVariable("pageNumber") int pageNumber , @Valid @PathVariable("pageSize") int pageSize) {
        var jwt = jwtUtil.getClaims();
        Long userId = jwt.getUserId();
        return ResponseEntity.ok(new ResponseData(HttpStatus.OK.value(), "success", userStatementService.getStatementAllStatusByUser(pageNumber,pageSize,userId)));
    }

}
