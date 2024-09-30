package com.banking.quantum.common.infra.exception;

public class LoanProcessingException extends RuntimeException {

    public LoanProcessingException(String message) {
        super(message);
    }
}