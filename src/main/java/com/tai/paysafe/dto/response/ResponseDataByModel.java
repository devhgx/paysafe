package com.tai.paysafe.dto.response;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ResponseDataByModel<T> {
    private int status;
    private String message;
    private T data;
}
