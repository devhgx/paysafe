package com.tai.paysafe.dto.response;

import com.tai.paysafe.constants.RegexpRequest;
import com.tai.paysafe.constants.ReponseErrorMessage;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;


@Data
public class RegistrationRequest {
    @NotNull(message = ReponseErrorMessage.USERNAME)
    @Pattern(regexp = RegexpRequest.USERNAME, message = ReponseErrorMessage.USERNAME)
    private String username;
    @NotNull(message = ReponseErrorMessage.USER_EMAIL)
    @Email(message = ReponseErrorMessage.USER_EMAIL)
    private String email;
    @NotNull(message = ReponseErrorMessage.USER_PASSWORD)
    @Pattern(regexp = RegexpRequest.USER_PASSWORD, message = ReponseErrorMessage.USER_PASSWORD)
    private String password;
    @NotNull
    @NotEmpty
    private String firstName;
    @NotNull
    @NotEmpty
    private String lastName;
    @NotNull
    @NotEmpty
    private String phoneNumber;
//    @NotNull
//    @NotEmpty
    private Long bankId;
    @NotNull
    @NotEmpty
    private String accountBankName;
    @NotNull
    @NotEmpty
    private String accountBankNumber;
}
