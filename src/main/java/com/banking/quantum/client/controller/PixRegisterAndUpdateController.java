package com.banking.quantum.client.controller;

import com.banking.quantum.client.domain.dto.transaction.PixKeyDto;
import com.banking.quantum.client.service.RegisterAndUpdatePixService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/quantumbanking/account/pix")
public class PixRegisterAndUpdateController {

    @Autowired
    private RegisterAndUpdatePixService pixService;

    @PostMapping("/register")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<String> regiterKeyPix(@RequestHeader("Authorization") String token,
                                                @RequestBody PixKeyDto pixKey) {

        pixService.registerKeyPix(token, pixKey);
        return ResponseEntity.ok("Chave Pix cadastrada com sucesso.");
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<String> updateKeyPix(@RequestHeader("Authorization") String token,
                                               @RequestBody PixKeyDto pixKey) {

        pixService.updatePixKey(token, pixKey);
        return ResponseEntity.ok("Chave Pix atualizada com sucesso.");
    }
}