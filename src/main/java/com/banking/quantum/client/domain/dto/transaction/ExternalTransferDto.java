package com.banking.quantum.client.domain.dto.transaction;

import com.banking.quantum.client.domain.account.AccountType;
import com.banking.quantum.client.domain.transaction.ExternalTransfer;
import com.banking.quantum.client.domain.transaction.TransactionType;

import java.math.BigDecimal;
import java.util.UUID;

public record ExternalTransferDto(UUID id,
                                  String name,
                                  Long accountOrigin,
                                  String accountDestiny,
                                  BigDecimal amount,
                                  TransactionType transactionType,
                                  String agencyNumber,
                                  String bankCode,
                                  String document,
                                  String accountType) {

    public ExternalTransferDto(ExternalTransfer externalTransfer) {
        this(externalTransfer.getId(),
                externalTransfer.getName(),
                externalTransfer.getAccountOrigin().getId(),
                externalTransfer.getAccountDestiny(),
                externalTransfer.getAmount(),
                externalTransfer.getType(),
                externalTransfer.getAgencyNumber(),
                externalTransfer.getBankCode(),
                externalTransfer.getDocument(),
                externalTransfer.getAccountType());
    }
}