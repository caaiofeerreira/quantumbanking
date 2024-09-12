package com.banking.quantum.common.infra.exception;

public class TransactionNotAuthorizedException extends RuntimeException {

    public TransactionNotAuthorizedException(String message) {
        super(message);
    }
}
