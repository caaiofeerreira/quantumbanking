package com.banking.quantum.client.service.impl;

import com.banking.quantum.client.domain.account.Account;
import com.banking.quantum.client.domain.account.AccountType;
import com.banking.quantum.client.domain.dto.loan.LoanDto;
import com.banking.quantum.client.domain.dto.loan.RequestLoanDto;
import com.banking.quantum.client.domain.loan.Loan;
import com.banking.quantum.client.domain.loan.LoanStatus;
import com.banking.quantum.client.domain.repository.LoanRepository;
import com.banking.quantum.client.service.RequestLoanService;
import com.banking.quantum.client.service.validate.ValidateRequestLoan;
import com.banking.quantum.common.infra.exception.LoanProcessingException;
import com.banking.quantum.common.infra.exception.UnauthorizedAccessException;
import com.banking.quantum.common.infra.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Service
public class RequestLoanServiceImpl implements RequestLoanService {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private ValidateRequestLoan validateRequestLoan;

    @Autowired
    private TokenService tokenService;

    @Override
    @Transactional
    public LoanDto requestLoan(String token, RequestLoanDto request) {

        try {

            Account account = tokenService.getAccountFromToken(token);

            validateRequestLoan.validate(account);

            Loan requestLoan = new Loan();
            requestLoan.setAccount(account);
            requestLoan.setAmount(request.amount());
            requestLoan.setInstallment(request.installment());
            requestLoan.setStatus(LoanStatus.PENDENTE);
            requestLoan.setCreatedAt(LocalDate.now());

            AccountType accountType = account.getType();
            BigDecimal interestRate = accountType.getInterestRate();

            BigDecimal totalAmount = validateRequestLoan.calculateTotalAmount(request.amount(), interestRate);

            requestLoan.setTotalLoanCost(totalAmount.setScale(2, RoundingMode.HALF_UP));
            requestLoan.setInterestRate(interestRate);

            loanRepository.save(requestLoan);

            return new LoanDto(requestLoan);

        } catch (LoanProcessingException e) {
            throw new LoanProcessingException("Erro ao processar pedido de empréstimo. ");
        } catch (UnauthorizedAccessException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Erro inesperado ao processar os dados.", e);
        }
    }
}