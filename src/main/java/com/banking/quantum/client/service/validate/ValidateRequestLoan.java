package com.banking.quantum.client.service.validate;

import com.banking.quantum.client.domain.account.Account;
import com.banking.quantum.client.domain.account.AccountStatus;
import com.banking.quantum.common.infra.exception.UnauthorizedAccessException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ValidateRequestLoan {

    public void validate(Account account) {

        if (account.getStatus() == AccountStatus.DESATIVADA) {
            throw new UnauthorizedAccessException("Sua conta está desativada, o que impede a solicitação de empréstimos. Para reativá-la, entre em contato com nosso suporte ou visite sua agência mais próxima.");
        }
    }

    public BigDecimal calculateTotalAmount(BigDecimal amount, BigDecimal interestRate) {
        return amount.add(amount.multiply(interestRate));
    }
}