package com.banking.quantum.manager.service;

import com.banking.quantum.client.domain.dto.loan.LoanDto;
import com.banking.quantum.manager.domain.dto.ApprovalRequest;

import java.util.List;

public interface LoanRequestsService {

    List<LoanDto> requestsLoans(String token);

    List<LoanDto> pendingLoanApplications(String token);

    List<LoanDto> canceledLoanApplications(String token);

    List<LoanDto> approvedLoans(String token);

    void approveLoan(String token, ApprovalRequest request);
}
