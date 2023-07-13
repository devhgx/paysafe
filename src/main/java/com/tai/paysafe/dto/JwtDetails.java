package com.tai.paysafe.dto;

import lombok.Data;

@Data
public class JwtDetails {
    private Long userId;
    private String role;
}
