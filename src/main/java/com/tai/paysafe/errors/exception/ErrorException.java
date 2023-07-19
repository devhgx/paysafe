package com.tai.paysafe.errors.exception;

public class ErrorException extends RuntimeException {
    public ErrorException(String message) {
        super(String.format(message));
    }
}
