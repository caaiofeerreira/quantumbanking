package com.banking.quantum.client.controller;

import com.banking.quantum.client.domain.dto.account.CloseAccountDto;
import com.banking.quantum.client.service.AccountClosureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/quantumbanking/account")
public class AccountClosureController {

    @Autowired
    private AccountClosureService accountClosureService;

    @PostMapping("/account-closure")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<CloseAccountDto> requestClosure(@RequestHeader("Authorization") String token) {

        CloseAccountDto closeAccount = accountClosureService.requestClosureAccount(token);
        return ResponseEntity.ok(closeAccount);
    }
}