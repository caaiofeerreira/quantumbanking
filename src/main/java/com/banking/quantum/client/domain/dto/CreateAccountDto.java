package com.banking.quantum.client.domain.dto;

import com.banking.quantum.client.domain.account.AccountType;
import com.banking.quantum.manager.domain.banking.Agency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateAccountDto(String accountNumber,
                               AccountType type,
                               BigDecimal balance,
                               String agencyNumber) {
}