package com.banking.quantum.client.domain.dto;


public record RegisterClientAndAccount(CreateClientDto clientDto,
                                       CreateAccountDto accountDto) {
}