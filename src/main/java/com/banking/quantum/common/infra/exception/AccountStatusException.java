package com.banking.quantum.common.infra.exception;

public class AccountStatusException extends RuntimeException {

    public AccountStatusException(String message) {
        super(message);
    }
}