package com.banking.quantum.client.domain.account;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public enum AccountType {

    CORRENTE(new BigDecimal("0.05")), // 5% juros;
    POUPANCA(new BigDecimal("0.03")), // 3% juros;
    JURIDICA(new BigDecimal("0.07")); // 7% juros;

    private final BigDecimal interestRate;

    AccountType(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
}