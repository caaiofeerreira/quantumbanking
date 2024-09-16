package com.banking.quantum.client.controller;

import com.banking.quantum.client.domain.dto.ExternalTransferDto;
import com.banking.quantum.client.domain.dto.InternalTransferDto;
import com.banking.quantum.client.domain.dto.PixTransferDto;
import com.banking.quantum.client.domain.dto.TransferOperationDto;
import com.banking.quantum.client.domain.service.BankTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/quantumbanking/account")
public class TransactionController {

    @Autowired
    private BankTransferService bankTransferService;

    @PostMapping("/transfer-internal")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<InternalTransferDto> processInternalTransfer(@RequestHeader("Authorization") String token,
                                                                       @RequestBody TransferOperationDto operationDto) {

        InternalTransferDto result = bankTransferService.processInternalTransfer(token, operationDto);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/transfer-external")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<ExternalTransferDto> processEternalTransfer(@RequestHeader("Authorization") String token,
                                                                      @RequestBody TransferOperationDto operationDto) {

        ExternalTransferDto result = bankTransferService.processExternalTransfer(token, operationDto);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/pix")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<PixTransferDto> processPixTransfer(@RequestHeader("Authorization") String token,
                                                             @RequestBody TransferOperationDto operationDto) {

        PixTransferDto result = bankTransferService.processPixTransfer(token, operationDto);
        return ResponseEntity.ok(result);
    }
}
