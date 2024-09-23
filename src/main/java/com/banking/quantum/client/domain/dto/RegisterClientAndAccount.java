package com.banking.quantum.client.domain.dto;

import com.banking.quantum.client.domain.dto.account.CreateAccountDto;
import com.banking.quantum.client.domain.dto.client.CreateClientDto;

public record RegisterClientAndAccount(CreateClientDto clientDto,
                                       CreateAccountDto accountDto) {
}