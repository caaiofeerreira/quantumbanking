package com.banking.quantum.client.domain.dto.account;

import com.banking.quantum.client.domain.account.Account;

import java.math.BigDecimal;

public record AccountDto(Long id,
                         String number,
                         BigDecimal balance,
                         String agencyNumber) {

    public AccountDto(Account account) {
        this(account.getId(),
                account.getAccountNumber(),
                account.getBalance(),
                account.getAgency().getNumber());
    }
}