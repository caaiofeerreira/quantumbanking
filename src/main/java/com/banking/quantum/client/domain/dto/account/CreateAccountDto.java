package com.banking.quantum.client.domain.dto.account;

import com.banking.quantum.client.domain.account.AccountType;

import java.math.BigDecimal;

public record CreateAccountDto(String accountNumber,
                               AccountType type,
                               BigDecimal balance,
                               String agencyNumber) {
}