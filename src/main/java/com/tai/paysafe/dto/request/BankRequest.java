package com.tai.paysafe.dto.request;

import lombok.Data;

@Data
public class BankRequest {
    private Long id;
    private String code;
    private String bankName;
}
