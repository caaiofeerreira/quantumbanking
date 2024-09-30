package com.banking.quantum.client.domain.dto.account;

import com.banking.quantum.client.domain.account.Account;

public record AccountClosureDto(Long request,
                                AccountDto accountDto) {

    public AccountClosureDto(Long request, Account account) {
        this(request, new AccountDto(account));
    }
}