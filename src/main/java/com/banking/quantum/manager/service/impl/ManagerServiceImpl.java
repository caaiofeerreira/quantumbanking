package com.banking.quantum.manager.service.impl;

import com.banking.quantum.client.domain.account.Account;
import com.banking.quantum.client.domain.account.AccountCloseStatus;
import com.banking.quantum.client.domain.account.AccountStatus;
import com.banking.quantum.client.domain.account.CloseAccount;
import com.banking.quantum.client.domain.dto.account.AccountClosureDto;
import com.banking.quantum.client.domain.dto.account.AccountDto;
import com.banking.quantum.client.domain.repository.AccountRepository;
import com.banking.quantum.client.domain.repository.CloseAccountRepository;
import com.banking.quantum.common.infra.exception.AccountNotFoundException;
import com.banking.quantum.common.infra.exception.UnauthorizedAccessException;
import com.banking.quantum.common.infra.security.TokenService;
import com.banking.quantum.manager.domain.dto.ApprovalRequest;
import com.banking.quantum.manager.domain.manager.Manager;
import com.banking.quantum.manager.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class ManagerServiceImpl implements ManagerService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CloseAccountRepository closeAccountRepository;

    @Autowired
    private TokenService tokenService;

    @Override
    @Transactional(readOnly = true)
    public List<AccountDto> getAccounts(String token) {

        try {
            Manager manager = tokenService.getManagerFromToken(token);

            List<Account> accounts = accountRepository.findByAgencyId(manager.getAgency().getId());

            if (accounts.isEmpty()) {
                throw new AccountNotFoundException("A agência ainda não possui clientes cadastrados.");
            }

            return accounts.stream()
                    .map(AccountDto::new).toList();

        } catch (AccountNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao obter contas", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountClosureDto> accountClosureRequest(String token) {

        try {
            Manager manager = tokenService.getManagerFromToken(token);

            List<Account> accounts = accountRepository.findByAgencyIdAndStatus(manager.getAgency().getId(), AccountStatus.DESATIVADA);

            List<AccountClosureDto> accountsPendingClosure = new ArrayList<>();

            for (Account account : accounts) {
                Optional<CloseAccount> closeAccountOpt = closeAccountRepository.findByAccountId(account.getId());

                if (closeAccountOpt.isPresent() && closeAccountOpt.get().getStatus() == AccountCloseStatus.PENDENTE) {
                    accountsPendingClosure.add(new AccountClosureDto(closeAccountOpt.get().getId(), account));
                }
            }

            if (accountsPendingClosure.isEmpty()) {
                throw new AccountNotFoundException("Nenhuma conta com pedido de encerramento pendente foi encontrada.");
            }

            accountsPendingClosure.sort(Comparator.comparing(AccountClosureDto::request));

            return accountsPendingClosure;

        } catch (AccountNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new UnauthorizedAccessException("Erro ao processar o token ou verificar permissões.");
        }
    }

    @Override
    @Transactional
    public void finalizeAccountClosure(String token, ApprovalRequest request) {

        try {
            Manager manager = tokenService.getManagerFromToken(token);

            CloseAccount closeAccount = closeAccountRepository.findById(request.id())
                    .orElseThrow(() -> new AccountNotFoundException("Pedido de encerramento não encontrado."));

            Account account = accountRepository.findById(closeAccount.getAccount().getId())
                    .orElseThrow(() -> new AccountNotFoundException("Conta associada ao pedido não encontrada."));

            if (!account.getAgency().getId().equals(manager.getAgency().getId())) {
                throw new UnauthorizedAccessException("Você não tem permissão para acessar esta conta.");
            }

            if (closeAccount.getStatus() == AccountCloseStatus.PENDENTE) {

                if (request.approve()) {
                    account.setStatus(AccountStatus.ENCERRADA);
                    closeAccount.setStatus(AccountCloseStatus.CANCELADO);
                } else {
                    account.setStatus(AccountStatus.ATIVA);
                    closeAccount.setStatus(AccountCloseStatus.REPROVADO);
                }

                accountRepository.save(account);
                closeAccount.setClosingDate(LocalDateTime.now());
                closeAccountRepository.save(closeAccount);
            } else {
                throw new UnauthorizedAccessException("Operação inválida: somente contas com status PENDENTE podem ser atualizados.");
            }
        } catch (AccountNotFoundException | UnauthorizedAccessException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar o token ou verificar permissões.");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountDto> closedAccounts(String token) {

        try {
            Manager manager = tokenService.getManagerFromToken(token);

            List<Account> accounts = accountRepository
                    .findByAgencyIdAndStatus(manager.getAgency().getId(), AccountStatus.ENCERRADA);

            if (accounts.isEmpty()) {
                throw new AccountNotFoundException("Nenhuma conta encerrada foi encontrada.");
            }

            return accounts.stream()
                    .map(AccountDto::new).toList();

        } catch (AccountNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new UnauthorizedAccessException("Erro ao processar o token ou verificar permissões.");
        }
    }
}