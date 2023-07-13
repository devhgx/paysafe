package com.tai.paysafe.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class WithdrawRequest {
    @NotEmpty
    @NotNull
    private String note;
//    @NotEmpty
//    @NotNull
    private BigDecimal amount;
//    @NotEmpty
//    @NotNull
    private Long userBankId;
}
