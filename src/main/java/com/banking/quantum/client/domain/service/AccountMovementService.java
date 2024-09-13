package com.banking.quantum.client.domain.service;

import com.banking.quantum.client.domain.dto.AccountMovementDto;
import com.banking.quantum.client.domain.dto.OperationAmountDto;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

public interface AccountMovementService {

    BigDecimal checkBalance(String token);

    AccountMovementDto deposit(String token, OperationAmountDto operationAmountDto);

    AccountMovementDto withdraw(String token, OperationAmountDto operationAmountDto);
}