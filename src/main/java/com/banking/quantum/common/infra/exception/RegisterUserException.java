package com.banking.quantum.common.infra.exception;

public class RegisterUserException extends RuntimeException {

    public RegisterUserException(String message) {
        super(message);
    }
}