package com.banking.quantum.client.controller;

import com.banking.quantum.client.domain.dto.loan.LoanDto;
import com.banking.quantum.client.domain.dto.loan.RequestLoanDto;
import com.banking.quantum.client.service.RequestLoanService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/quantumbanking/account/loan")
public class RequestLoanController {

    @Autowired
    private RequestLoanService loanService;

    @PostMapping
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<LoanDto> requestLoan(@RequestHeader("Authorization") String token,
                                               @RequestBody RequestLoanDto request) {

        LoanDto newLoan = loanService.requestLoan(token, request);
        return ResponseEntity.status(HttpServletResponse.SC_CREATED).body(newLoan);
    }
}