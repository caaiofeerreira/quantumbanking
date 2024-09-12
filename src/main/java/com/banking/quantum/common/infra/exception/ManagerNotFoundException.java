package com.banking.quantum.common.infra.exception;

public class ManagerNotFoundException extends RuntimeException {

    public ManagerNotFoundException(String message) {
        super(message);
    }
}