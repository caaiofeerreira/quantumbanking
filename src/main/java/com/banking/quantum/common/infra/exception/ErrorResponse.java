package com.banking.quantum.common.infra.exception;

import java.time.LocalDateTime;

public record ErrorResponse(int status, String message, LocalDateTime timestamp) {
}