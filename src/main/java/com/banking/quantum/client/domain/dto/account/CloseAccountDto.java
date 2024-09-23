package com.banking.quantum.client.domain.dto.account;

import com.banking.quantum.client.domain.account.AccountCloseStatus;
import com.banking.quantum.client.domain.account.CloseAccount;

import java.time.LocalDateTime;

public record CloseAccountDto(Long id,
                              AccountCloseStatus status,
                              LocalDateTime closingDate) {

    public CloseAccountDto(CloseAccount closeAccount) {
        this(closeAccount.getId(),
                closeAccount.getStatus(),
                closeAccount.getClosingDate());
    }
}