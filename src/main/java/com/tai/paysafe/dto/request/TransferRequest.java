package com.tai.paysafe.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferRequest {
    @NotEmpty
    @NotNull
    private String recipientUserName;
    @NotEmpty
    @NotNull
    private String note;
//    @NotEmpty
//    @NotNull
    private BigDecimal amount;
}
