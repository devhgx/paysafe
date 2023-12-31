package com.tai.paysafe.controller;

import com.tai.paysafe.constants.ResponseStatusMessage;
import com.tai.paysafe.constants.RoleType;
import com.tai.paysafe.dto.response.RegistrationRequest;
import com.tai.paysafe.dto.response.ResponseData;
import com.tai.paysafe.entities.User;
import com.tai.paysafe.entities.UserBank;
import com.tai.paysafe.repository.UserRepositoryCustomImpl;
import com.tai.paysafe.service.UserService;
import com.tai.paysafe.service.UserStatementService;
import com.tai.paysafe.utils.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    @Qualifier("userServiceImpl")
    private UserService userService;

    @Autowired
    private UserStatementService userStatementService;

    @Autowired
    private UserRepositoryCustomImpl userRepositoryCustom;


    // register user profile
    @PostMapping("/register")
    public ResponseEntity<ResponseData> register(@Valid @RequestBody RegistrationRequest registrationRequest) {
        User data = userService.create(registrationRequest, RoleType.USER);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseData(HttpStatus.OK.value(), ResponseStatusMessage.SUCCESS, data));
    }

    @GetMapping("/test")
    public ResponseEntity<ResponseData> registerAdmin() {
        var data = userRepositoryCustom.getUserSummaryModelByUsername("buyyer");
        return ResponseEntity.ok(new ResponseData(HttpStatus.OK.value(), ResponseStatusMessage.SUCCESS, data));
    }

    // get user profile
    @GetMapping("/getProfile")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseData> getProfile() {
        Long userId = jwtUtil.getClaims().getUserId();
        User data = userService.findById(userId);
        return ResponseEntity.ok(new ResponseData(HttpStatus.OK.value(), ResponseStatusMessage.SUCCESS, data));
    }

    @GetMapping("/getUserBanks")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ResponseData> getUserBanks() {
        Long userId = jwtUtil.getClaims().getUserId();
        List<UserBank> data = userService.getUserBanks(userId);
        return ResponseEntity.ok(new ResponseData(HttpStatus.OK.value(), ResponseStatusMessage.SUCCESS, data));
    }


}
