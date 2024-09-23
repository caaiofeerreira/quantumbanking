package com.banking.quantum.client.repository;

import com.banking.quantum.client.domain.account.AccountCloseStatus;
import com.banking.quantum.client.domain.account.CloseAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface CloseAccountRepository extends JpaRepository<CloseAccount, Long> {

    Optional<CloseAccount> findByAccountId(Long id);
}