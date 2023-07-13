package com.tai.paysafe.controller;

import com.tai.paysafe.dto.request.LoginRequest;
import com.tai.paysafe.dto.request.TokenRefreshRequest;
import com.tai.paysafe.dto.response.JwtResponse;
import com.tai.paysafe.dto.response.ResponseData;
import com.tai.paysafe.dto.response.TokenRefreshResponse;
import com.tai.paysafe.entities.RefreshToken;
import com.tai.paysafe.errors.exception.TokenRefreshException;
import com.tai.paysafe.service.RefreshTokenService;
import com.tai.paysafe.dto.UserDetailsImpl;
import com.tai.paysafe.utils.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthenController {
    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtil.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        return ResponseEntity.ok(new ResponseData(HttpStatus.OK.value(), "success", new JwtResponse(refreshToken.getToken(), jwt)));

    }

    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String authorizationHeader, @Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();
        return refreshTokenService.findByToken(requestRefreshToken).map(refreshTokenService::verifyExpiration).map(RefreshToken::getUser).map(user -> {
            String token = jwtUtil.generateToken(user.getId().toString(), user.getRole());
            return ResponseEntity.ok(new ResponseData(HttpStatus.OK.value(), "success", new TokenRefreshResponse(token, requestRefreshToken)));
        }).orElseThrow(() -> new TokenRefreshException(requestRefreshToken, "Refresh token is not in database!"));
    }

    @GetMapping("/logout")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseData> logout() {
        Long userId = jwtUtil.getClaims().getUserId();
        return ResponseEntity.ok(new ResponseData(HttpStatus.OK.value(), "success", refreshTokenService.deleteRefreshToken(userId)));
    }
}