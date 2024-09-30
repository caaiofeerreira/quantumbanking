package com.banking.quantum.client.domain.dto.loan;

import com.banking.quantum.client.domain.loan.Loan;
import com.banking.quantum.client.domain.loan.LoanStatus;

import java.math.BigDecimal;

public record LoanDto(Long id,
                      BigDecimal amount,
                      int installment,
                      BigDecimal totalLoanCost,
                      LoanStatus status) {

    public LoanDto(Loan loan) {
        this(loan.getId(),
                loan.getAmount(),
                loan.getInstallment(),
                loan.getTotalLoanCost(),
                loan.getStatus());
    }
}