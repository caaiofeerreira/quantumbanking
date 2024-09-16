package com.banking.quantum.client.domain.dto;

import com.banking.quantum.client.domain.transaction.InternalTransfer;
import com.banking.quantum.client.domain.transaction.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record InternalTransferDto(UUID id,
                                  Long accountOriginId,
                                  Long accountDestinyId,
                                  BigDecimal amount,
                                  TransactionType type,
                                  LocalDateTime createdAt) {

    public InternalTransferDto(InternalTransfer internalTransfer) {
        this(internalTransfer.getId(),
                internalTransfer.getAccountOrigin().getId(),
                internalTransfer.getAccountDestiny().getId(),
                internalTransfer.getAmount(),
                internalTransfer.getType(),
                internalTransfer.getCreatedAt());
    }
}