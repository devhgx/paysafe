package com.tai.paysafe.dto.request;

import com.tai.paysafe.constants.RegexpRequest;
import com.tai.paysafe.constants.ReponseErrorMessage;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @NotNull(message = ReponseErrorMessage.USERNAME)
    @Pattern(regexp = RegexpRequest.USERNAME, message = ReponseErrorMessage.USERNAME)
    private String username;
    @NotNull(message = ReponseErrorMessage.USER_PASSWORD)
    @Pattern(regexp = RegexpRequest.USER_PASSWORD, message = ReponseErrorMessage.USER_PASSWORD)
    private String password;
}
