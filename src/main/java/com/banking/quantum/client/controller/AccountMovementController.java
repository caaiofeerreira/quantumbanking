package com.banking.quantum.client.controller;

import com.banking.quantum.client.domain.dto.AccountMovementDto;
import com.banking.quantum.client.domain.dto.OperationAmountDto;
import com.banking.quantum.client.domain.service.AccountMovementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

@RestController
@RequestMapping("/quantumbanking/account")
public class AccountMovementController {

    @Autowired
    private AccountMovementService accountMovementService;

    @GetMapping("/balance")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<String> getBalance(@RequestHeader("Authorization") String token) {

        BigDecimal balance = accountMovementService.checkBalance(token);
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        String formatBalance = numberFormat.format(balance);

        return ResponseEntity.ok(String.format("Saldo: %s", formatBalance));
    }

    @PostMapping("/deposit")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<AccountMovementDto> processDeposit(@RequestHeader("Authorization") String token,
                                                             @RequestBody OperationAmountDto operationAmountDto) {

        AccountMovementDto movementDto = accountMovementService.deposit(token, operationAmountDto);
        return ResponseEntity.ok(movementDto);
    }

    @PostMapping("/withdraw")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<AccountMovementDto> processWithdraw(@RequestHeader("Authorization") String token,
                                                              @RequestBody OperationAmountDto operationAmountDto) {

        AccountMovementDto movementDto = accountMovementService.withdraw(token, operationAmountDto);
        return ResponseEntity.ok(movementDto);
    }
}