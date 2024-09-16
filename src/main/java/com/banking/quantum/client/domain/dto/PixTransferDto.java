package com.banking.quantum.client.domain.dto;

import com.banking.quantum.client.domain.transaction.PixTransfer;
import com.banking.quantum.client.domain.transaction.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PixTransferDto(UUID id,
                             Long accountOrigin,
                             Long accountDestiny,
                             BigDecimal amount,
                             TransactionType type,
                             String pixKey,
                             LocalDateTime createdAt) {

    public PixTransferDto(PixTransfer pixTransfer) {
        this(pixTransfer.getId(),
                pixTransfer.getAccountOrigin().getId(),
                pixTransfer.getAccountDestiny() != null ? pixTransfer.getAccountDestiny().getId() : null,
                pixTransfer.getAmount(),
                pixTransfer.getType(),
                pixTransfer.getPixKey(),
                pixTransfer.getCreatedAt());
    }
}