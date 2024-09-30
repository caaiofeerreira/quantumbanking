package com.banking.quantum.client.domain.dto;

import com.banking.quantum.client.domain.account.AccountType;
import com.banking.quantum.client.domain.client.ClientType;
import com.banking.quantum.common.domain.address.Address;

import java.math.BigDecimal;

public record CreateClientAccountDto(String name,
                                     String cpf,
                                     String phone,
                                     String email,
                                     String password,
                                     Address address,
                                     ClientType clientType,
                                     String accountNumber,
                                     AccountType accountType,
                                     BigDecimal balance,
                                     String agencyNumber) {
}