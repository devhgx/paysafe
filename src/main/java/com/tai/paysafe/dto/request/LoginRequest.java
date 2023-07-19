package com.tai.paysafe.dto.request;

import com.tai.paysafe.constants.RegexpRequest;
import com.tai.paysafe.constants.ResponseErrorMessage;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @NotNull(message = ResponseErrorMessage.USERNAME)
    @Pattern(regexp = RegexpRequest.USERNAME, message = ResponseErrorMessage.USERNAME)
    private String username;
    @NotNull(message = ResponseErrorMessage.USER_PASSWORD)
    @Pattern(regexp = RegexpRequest.USER_PASSWORD, message = ResponseErrorMessage.USER_PASSWORD)
    private String password;
}
