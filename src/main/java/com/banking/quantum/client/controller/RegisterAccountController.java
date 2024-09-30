package com.banking.quantum.client.controller;

import com.banking.quantum.client.domain.dto.CreateClientAccountDto;
import com.banking.quantum.client.service.RegisterAccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/quantumbanking/register")
public class RegisterAccountController {

    @Autowired
    private RegisterAccountService registerService;

    @PostMapping("/account")
    public ResponseEntity<String> createAccount(@RequestBody @Valid CreateClientAccountDto clientAndAccountDto) {

        registerService.registerAccount(clientAndAccountDto);
        return ResponseEntity.status(HttpStatus.OK).body("Sua conta foi registrada com sucesso.");
    }
}