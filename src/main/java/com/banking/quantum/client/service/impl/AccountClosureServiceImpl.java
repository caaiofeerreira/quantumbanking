package com.banking.quantum.client.service.impl;

import com.banking.quantum.client.domain.account.Account;
import com.banking.quantum.client.domain.account.AccountCloseStatus;
import com.banking.quantum.client.domain.account.AccountStatus;
import com.banking.quantum.client.domain.account.CloseAccount;
import com.banking.quantum.client.domain.dto.account.CloseAccountDto;
import com.banking.quantum.client.domain.repository.AccountRepository;
import com.banking.quantum.client.domain.repository.CloseAccountRepository;
import com.banking.quantum.client.service.AccountClosureService;
import com.banking.quantum.client.service.validate.ValidateAccountClosure;
import com.banking.quantum.common.infra.exception.UnauthorizedAccessException;
import com.banking.quantum.common.infra.exception.ValidateException;
import com.banking.quantum.common.infra.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AccountClosureServiceImpl implements AccountClosureService {

    @Autowired
    private CloseAccountRepository closeAccountRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ValidateAccountClosure validateAccountClosure;

    @Autowired
    private TokenService tokenService;

    @Override
    @Transactional
    public CloseAccountDto requestClosureAccount(String token) {

        try {
            Account account = tokenService.getAccountFromToken(token);

            validateAccountClosure.validate(account);

            CloseAccount request = new CloseAccount();
            request.setAccount(account);
            request.setStatus(AccountCloseStatus.PENDENTE);
            request.setClosingDate(LocalDateTime.now());

            account.setStatus(AccountStatus.DESATIVADA);
            accountRepository.save(account);

            closeAccountRepository.save(request);

            return new CloseAccountDto(request);

        } catch (ValidateException e) {
            throw e;
        } catch (Exception e) {
            throw new UnauthorizedAccessException("Nao autorizado. " + e.getMessage());
        }
    }
}