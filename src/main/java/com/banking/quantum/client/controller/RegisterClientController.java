package com.banking.quantum.client.controller;

import com.banking.quantum.client.domain.dto.ClientDto;
import com.banking.quantum.client.domain.dto.CreateAccountDto;
import com.banking.quantum.client.domain.dto.CreateClientDto;
import com.banking.quantum.client.domain.dto.RegisterClientAndAccount;
import com.banking.quantum.client.domain.service.RegisterClientAndAccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/quantumbanking/client")
public class RegisterClientController {

    @Autowired
    private RegisterClientAndAccountService registerService;

    @PostMapping("/register")
    public ResponseEntity<ClientDto> createAccount(@RequestBody @Valid RegisterClientAndAccount clientAndAccountDto) {

        CreateClientDto client = clientAndAccountDto.clientDto();
        CreateAccountDto account = clientAndAccountDto.accountDto();

        ClientDto newClient = registerService.registerClient(client, account);

        return ResponseEntity.status(HttpStatus.CREATED).body(newClient);
    }
}