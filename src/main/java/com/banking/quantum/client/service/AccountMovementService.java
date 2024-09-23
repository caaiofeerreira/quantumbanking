package com.banking.quantum.client.service;

import com.banking.quantum.client.domain.dto.account.AccountMovementDto;
import com.banking.quantum.client.domain.dto.transaction.OperationAmountDto;

import java.math.BigDecimal;

public interface AccountMovementService {

    BigDecimal checkBalance(String token);

    AccountMovementDto deposit(String token, OperationAmountDto operationAmountDto);

    AccountMovementDto withdraw(String token, OperationAmountDto operationAmountDto);
}