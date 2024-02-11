package com.cydeo.exception;

public class NoEnoughBalanceException extends RuntimeException {
    public NoEnoughBalanceException(String message) {
        super(message);
    }
}
