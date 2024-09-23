package com.banking.quantum.manager.controller;

import com.banking.quantum.client.domain.dto.account.AccountClosureDto;
import com.banking.quantum.client.domain.dto.account.AccountDto;
import com.banking.quantum.manager.domain.dto.ApproveClosure;
import com.banking.quantum.manager.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quantumbanking/dashboard")
public class ManagerAccountController {

    @Autowired
    private ManagerService managerService;

    @GetMapping("/accounts")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<AccountDto>> getAccounts(@RequestHeader("Authorization") String token) {

        List<AccountDto> accounts = managerService.getAccounts(token);
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/accounts-closure")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<AccountClosureDto>> getAccountClosureRequest(@RequestHeader("Authorization") String token) {

        List<AccountClosureDto> accounts = managerService.accountClosureRequest(token);
        return ResponseEntity.ok(accounts);
    }

    @PutMapping("/finalizer-account")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Void> closeAccount(@RequestHeader("Authorization") String token,
                                             @RequestBody ApproveClosure request) {

        managerService.finalizeAccountClosure(token, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/closed-accounts")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<AccountDto>> getClosedAccounts(@RequestHeader("Authorization") String token) {

        List<AccountDto> accounts = managerService.closedAccounts(token);
        return ResponseEntity.ok(accounts);
    }
}