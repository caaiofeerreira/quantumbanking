package com.banking.quantum.common.infra.exception;

public class ClientAccountCreationException extends RuntimeException {

    public ClientAccountCreationException(String message) {
        super(message);
    }
}