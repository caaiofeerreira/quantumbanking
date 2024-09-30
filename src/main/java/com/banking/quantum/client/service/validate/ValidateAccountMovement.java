package com.banking.quantum.client.service.validate;

import com.banking.quantum.client.domain.account.Account;
import com.banking.quantum.client.domain.account.AccountStatus;
import com.banking.quantum.client.domain.repository.AccountRepository;
import com.banking.quantum.common.infra.exception.AccountStatusException;
import com.banking.quantum.common.infra.exception.TransactionNotAuthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ValidateAccountMovement {

    @Autowired
    private AccountRepository accountRepository;

    private static final BigDecimal MINIMUM_AMOUNT = new BigDecimal("10.00");

    public void validate(Account account, BigDecimal amount) {

        if (account.getStatus() == AccountStatus.DESATIVADA) {
            throw new AccountStatusException("A operação não pode ser concluída porque sua conta está desativada. Por favor, entre em contato com o suporte para mais informações ou vá até sua agência para assistência adicional.");
        }

        if (amount.compareTo(MINIMUM_AMOUNT) < 0) {
            throw new TransactionNotAuthorizedException("O valor mínimo para saque e deposito é R$ " + MINIMUM_AMOUNT);
        }
    }
}