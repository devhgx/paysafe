package com.tai.paysafe.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtResponse {
    private String refreshToken;
    private String accessToken;
//    private Object roles;
}

