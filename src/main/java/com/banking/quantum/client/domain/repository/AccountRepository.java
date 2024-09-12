package com.banking.quantum.client.domain.repository;

import com.banking.quantum.client.domain.account.Account;
import com.banking.quantum.client.domain.client.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query("SELECT a FROM Account a WHERE a.accountNumber = :accountNumber")
    Optional<Account> findByAccountNumber(@Param("accountNumber") String accountNumber);

    @Query("SELECT a.balance FROM Account a WHERE a.id = :id")
    Optional<BigDecimal> findBalanceById(@Param("id") Long id);

    @Query("SELECT c FROM Client c WHERE c.cpf = :cpf OR c.email = :email OR c.phone = :phone")
    Optional<Client> findByCpfOrEmailOrPhone(@Param("cpf") String cpf,
                                             @Param("email") String email,
                                             @Param("phone") String phone);

}