package com.tai.paysafe.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Data
public class ResponseData {
    private int status;
    private String message;
    private Object data;
}

