package com.banking.quantum.client.service;

import com.banking.quantum.client.domain.dto.transaction.ExternalTransferDto;
import com.banking.quantum.client.domain.dto.transaction.InternalTransferDto;
import com.banking.quantum.client.domain.dto.transaction.PixTransferDto;
import com.banking.quantum.client.domain.dto.transaction.TransferOperationDto;

public interface BankTransferService {

    InternalTransferDto processInternalTransfer(String token, TransferOperationDto operationDto);

    ExternalTransferDto processExternalTransfer(String token, TransferOperationDto operationDto);

    PixTransferDto processPixTransfer(String token, TransferOperationDto operationDto);
}