package com.banking.quantum.manager.controller;

import com.banking.quantum.manager.domain.dto.CreateManagerDto;
import com.banking.quantum.manager.domain.dto.ManagerDto;
import com.banking.quantum.manager.service.RegisterManagerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/quantumbanking/manager")
public class RegisterManagerController {

    @Autowired
    private RegisterManagerService registerService;

    @PostMapping("/register")
    public ResponseEntity<ManagerDto> createAccount(@RequestBody @Valid CreateManagerDto managerDto) {

        ManagerDto manager = registerService.registerManager(managerDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(manager);
    }
}