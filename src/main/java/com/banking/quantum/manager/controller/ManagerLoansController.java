package com.banking.quantum.manager.controller;

import com.banking.quantum.client.domain.dto.loan.LoanDto;
import com.banking.quantum.manager.domain.dto.ApprovalRequest;
import com.banking.quantum.manager.service.LoanRequestsService;
import org.aspectj.weaver.patterns.IToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quantumbanking/dashboard/loan")
public class ManagerLoansController {

    @Autowired
    private LoanRequestsService loanRequests;

    @GetMapping("/requests")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<LoanDto>> getRequestsLoans(@RequestHeader("Authorization") String token) {

        List<LoanDto> loans = loanRequests.requestsLoans(token);
        return ResponseEntity.ok().body(loans);
    }

    @GetMapping("/pending")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<LoanDto>> getPendingLoans(@RequestHeader("Authorization") String token) {

        List<LoanDto> loans = loanRequests.pendingLoanApplications(token);
        return ResponseEntity.ok().body(loans);
    }

    @GetMapping("/canceled")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<LoanDto>> getCanceledLoans(@RequestHeader("Authorization") String token) {

        List<LoanDto> loans = loanRequests.canceledLoanApplications(token);
        return ResponseEntity.ok().body(loans);
    }

    @GetMapping("/approved")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<LoanDto>> getApprovedLoans(@RequestHeader("Authorization") String token) {

        List<LoanDto> loans = loanRequests.approvedLoans(token);
        return ResponseEntity.ok().body(loans);
    }

    @PutMapping("/approve")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Void> approveLoanRequest(@RequestHeader("Authorization") String token,
                                                   @RequestBody ApprovalRequest request) {

        loanRequests.approveLoan(token, request);
        return ResponseEntity.ok().build();
    }
}