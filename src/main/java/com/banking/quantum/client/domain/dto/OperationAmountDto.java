package com.banking.quantum.client.domain.dto;

import com.banking.quantum.client.domain.transaction.TransactionType;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record OperationAmountDto(@Positive BigDecimal amount, TransactionType type) {
}