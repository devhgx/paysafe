package com.tai.paysafe.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DepositRequest {
//    @NotEmpty
//    @NotNull
    private Long depositFromBankId;
    @NotEmpty
    @NotNull
    private String depositFromAccountBankNumber;
    @NotEmpty
    @NotNull
    private String depositFromAccountName;
    @NotEmpty
    @NotNull
    private String note;
//    @NotEmpty
//    @NotNull
    private BigDecimal amount;
}
