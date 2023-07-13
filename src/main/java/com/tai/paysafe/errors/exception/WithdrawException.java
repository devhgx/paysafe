package com.tai.paysafe.errors.exception;

public class WithdrawException extends RuntimeException {
    public WithdrawException(String message) {
        super(String.format(message));
    }
}
