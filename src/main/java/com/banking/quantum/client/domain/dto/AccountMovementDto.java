package com.banking.quantum.client.domain.dto;

import com.banking.quantum.client.domain.transaction.AccountMovement;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record AccountMovementDto(UUID id,
                                 BigDecimal amount,
                                 LocalDateTime createdAt) {

    public AccountMovementDto(AccountMovement accountMovement) {
        this(accountMovement.getId(),
                accountMovement.getAmount(),
                accountMovement.getCreatedAt());
    }
}