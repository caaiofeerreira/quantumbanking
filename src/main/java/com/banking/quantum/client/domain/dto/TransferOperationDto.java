package com.banking.quantum.client.domain.dto;

import com.banking.quantum.client.domain.account.AccountType;
import com.banking.quantum.client.domain.transaction.PixType;

import java.math.BigDecimal;

public record TransferOperationDto(String name,
                                   String accountDestiny,
                                   BigDecimal amount,
                                   AccountType accountType,
                                   String agencyNumber,
                                   String bankCode,
                                   String document,
                                   String cpf,
                                   String phone,
                                   String email,
                                   String pixKey,
                                   PixType pixType) {
}