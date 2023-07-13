package com.tai.paysafe.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ApproveRequest {
//    @NotNull
//    @NotEmpty
    private Long transactionId;
}
