package com.banking.quantum.client.service.validate;

import com.banking.quantum.client.domain.account.Account;
import com.banking.quantum.common.infra.exception.UnauthorizedAccessException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ValidateAccountClosure {

    public void validate(Account account) {

        if (account.getBalance().compareTo(BigDecimal.ZERO) > 0) {
            throw new UnauthorizedAccessException("Para encerrar sua conta, é preciso sacar todo o dinheiro.");
        }
    }
}
