package com.banking.quantum.client.service.impl;

import com.banking.quantum.client.domain.account.Account;
import com.banking.quantum.client.domain.dto.account.AccountMovementDto;
import com.banking.quantum.client.domain.dto.transaction.OperationAmountDto;
import com.banking.quantum.client.repository.AccountMovementRepository;
import com.banking.quantum.client.repository.AccountRepository;
import com.banking.quantum.client.service.AccountMovementService;
import com.banking.quantum.client.service.validate.ValidateAccountMovement;
import com.banking.quantum.client.domain.transaction.AccountMovement;
import com.banking.quantum.client.domain.transaction.TransactionType;
import com.banking.quantum.common.infra.exception.AccountStatusException;
import com.banking.quantum.common.infra.exception.TransactionNotAuthorizedException;
import com.banking.quantum.common.infra.exception.UnauthorizedAccessException;
import com.banking.quantum.common.infra.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class AccountMovementServiceImpl implements AccountMovementService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountMovementRepository movementRepository;

    @Autowired
    private ValidateAccountMovement validateAccountMovement;

    @Autowired
    private TokenService tokenService;

    @Override
    public BigDecimal checkBalance(String token) {

        Account account = tokenService.getAccountFromToken(token);

        return accountRepository.findBalanceById(account.getId())
                .orElseThrow(() -> new UnauthorizedAccessException("Não foi possível validar o token ou encontrar o saldo da conta. Verifique o token fornecido e tente novamente."));
    }

    @Override
    @Transactional
    public AccountMovementDto deposit(String token, OperationAmountDto operationAmountDto) {

        try {
            Account account = tokenService.getAccountFromToken(token);

            validateAccountMovement.validate(account, operationAmountDto.amount());

            AccountMovement movement = new AccountMovement();
            movement.setAccount(account);
            movement.setAmount(operationAmountDto.amount());
            movement.setType(TransactionType.DEPOSITO);
            movement.setCreatedAt(LocalDateTime.now());

            account.credit(operationAmountDto.amount());

            accountRepository.save(account);
            movementRepository.save(movement);

            return new AccountMovementDto(movement);

        } catch (TransactionNotAuthorizedException | AccountStatusException e) {
            throw e;
        }
    }

    @Override
    @Transactional
    public AccountMovementDto withdraw(String token, OperationAmountDto operationAmountDto) {

        try {
            Account account = tokenService.getAccountFromToken(token);

            validateAccountMovement.validate(account, operationAmountDto.amount());

            AccountMovement movement = new AccountMovement();
            movement.setAccount(account);
            movement.setAmount(operationAmountDto.amount());
            movement.setType(TransactionType.SAQUE);
            movement.setCreatedAt(LocalDateTime.now());

            account.debit(operationAmountDto.amount());

            accountRepository.save(account);
            movementRepository.save(movement);

            return new AccountMovementDto(movement);

        } catch (TransactionNotAuthorizedException | AccountStatusException e) {
            throw e;
        }
    }
}