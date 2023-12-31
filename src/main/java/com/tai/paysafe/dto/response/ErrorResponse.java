package com.tai.paysafe.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.List;
@Data
@AllArgsConstructor
public class ErrorResponse {
    private int status;
    private String message;
    private List<String> data;
}