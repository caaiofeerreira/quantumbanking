package com.banking.quantum.client.service.validate;

import com.banking.quantum.client.domain.account.Account;
import com.banking.quantum.client.domain.loan.Loan;
import com.banking.quantum.client.domain.loan.LoanStatus;
import com.banking.quantum.client.domain.repository.LoanRepository;
import com.banking.quantum.common.infra.exception.UnauthorizedAccessException;
import com.banking.quantum.common.infra.exception.ValidateException;
import org.apache.commons.validator.ValidatorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Component
public class ValidateAccountClosure {

    @Autowired
    private LoanRepository loanRepository;

    public void validate(Account account) {

        List<Loan> loans = loanRepository.findByAccountIdAndStatus(account.getId(), LoanStatus.APROVADO);

        if (!loans.isEmpty()) {
            throw new UnauthorizedAccessException("Você não pode encerrar sua conta enquanto tiver um empréstimo em aberto. Recomendamos que quite seu empréstimo antes de solicitar o encerramento.");
        }

        if (account.getBalance().compareTo(BigDecimal.ZERO) > 0) {
            throw new ValidateException("Para encerrar sua conta, é preciso sacar todo o dinheiro.");
        }
    }
}
