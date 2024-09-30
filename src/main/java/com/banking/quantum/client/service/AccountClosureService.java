package com.banking.quantum.client.service;

import com.banking.quantum.client.domain.dto.account.CloseAccountDto;

public interface AccountClosureService {

    CloseAccountDto requestClosureAccount(String token);
}