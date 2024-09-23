package com.banking.quantum.client.controller;

import com.banking.quantum.client.domain.dto.client.ClientDto;
import com.banking.quantum.client.service.ClientUpdateService;
import com.banking.quantum.common.domain.dto.UserUpdateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/quantumbanking/account")
public class ClientUpdateController {

    @Autowired
    private ClientUpdateService clientUpdateService;

    @PutMapping("/update-profile")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<ClientDto> updateProfileClient(@RequestHeader("Authorization") String token,
                                                         @RequestBody UserUpdateDto updateDto) {

        ClientDto updatedClient = clientUpdateService.updateClient(token, updateDto);
        return ResponseEntity.ok(updatedClient);
    }
}