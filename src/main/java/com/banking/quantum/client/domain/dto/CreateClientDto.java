package com.banking.quantum.client.domain.dto;

import com.banking.quantum.client.domain.account.Account;
import com.banking.quantum.client.domain.client.ClientType;
import com.banking.quantum.common.domain.address.Address;

public record CreateClientDto(String name,
                              String cpf,
                              String phone,
                              String email,
                              String password,
                              Address address,
                              ClientType type,
                              Account account) {
}