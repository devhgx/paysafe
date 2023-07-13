package com.tai.paysafe.errors.exception;

public class ApproveException extends RuntimeException {
    public ApproveException(String message) {
        super(String.format(message));
    }
}
