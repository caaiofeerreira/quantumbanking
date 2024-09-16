package com.banking.quantum.client.domain.service;

import com.banking.quantum.client.domain.dto.ExternalTransferDto;
import com.banking.quantum.client.domain.dto.InternalTransferDto;
import com.banking.quantum.client.domain.dto.PixTransferDto;
import com.banking.quantum.client.domain.dto.TransferOperationDto;

public interface BankTransferService {

    InternalTransferDto processInternalTransfer(String token, TransferOperationDto operationDto);

    ExternalTransferDto processExternalTransfer(String token, TransferOperationDto operationDto);

    PixTransferDto processPixTransfer(String token, TransferOperationDto operationDto);
}