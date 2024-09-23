package com.banking.quantum.client.repository;

import com.banking.quantum.client.domain.account.Account;
import com.banking.quantum.client.domain.account.AccountStatus;
import com.banking.quantum.client.domain.client.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query("SELECT a FROM Account a WHERE a.accountNumber = :accountNumber")
    Optional<Account> findByAccountNumber(@Param("accountNumber") String accountNumber);

    @Query("SELECT a.balance FROM Account a WHERE a.id = :id")
    Optional<BigDecimal> findBalanceById(@Param("id") Long id);

    List<Account> findByAgencyId(Long id);

    @Query("SELECT a FROM Account a WHERE a.agency.id = :agencyId AND a.status = :status")
    List<Account> findByAgencyIdAndStatus(@Param("agencyId") Long agencyId, @Param("status") AccountStatus status);
}