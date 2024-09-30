package com.banking.quantum.client.service;

import com.banking.quantum.client.domain.dto.CreateClientAccountDto;

public interface RegisterAccountService {

    void registerAccount(CreateClientAccountDto createClientAccountDto);

}