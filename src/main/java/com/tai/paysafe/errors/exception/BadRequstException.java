package com.tai.paysafe.errors.exception;

public class BadRequstException extends RuntimeException {
    public BadRequstException(String message) {
        super(String.format(message));
    }
}
