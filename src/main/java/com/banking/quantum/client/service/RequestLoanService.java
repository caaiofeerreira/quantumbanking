package com.banking.quantum.client.service;

import com.banking.quantum.client.domain.dto.loan.LoanDto;
import com.banking.quantum.client.domain.dto.loan.RequestLoanDto;

public interface RequestLoanService {

    LoanDto requestLoan(String token, RequestLoanDto request);
}