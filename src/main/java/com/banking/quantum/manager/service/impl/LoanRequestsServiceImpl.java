package com.banking.quantum.manager.service.impl;

import com.banking.quantum.client.domain.account.Account;
import com.banking.quantum.client.domain.dto.loan.LoanDto;
import com.banking.quantum.client.domain.loan.Loan;
import com.banking.quantum.client.domain.loan.LoanStatus;
import com.banking.quantum.client.domain.repository.AccountRepository;
import com.banking.quantum.client.domain.repository.LoanRepository;
import com.banking.quantum.common.infra.exception.AccountNotFoundException;
import com.banking.quantum.common.infra.exception.LoanProcessingException;
import com.banking.quantum.common.infra.exception.UnauthorizedAccessException;
import com.banking.quantum.common.infra.security.TokenService;
import com.banking.quantum.manager.domain.dto.ApprovalRequest;
import com.banking.quantum.manager.domain.manager.Manager;
import com.banking.quantum.manager.service.LoanRequestsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LoanRequestsServiceImpl implements LoanRequestsService {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TokenService tokenService;

    @Override
    @Transactional(readOnly = true)
    public List<LoanDto> requestsLoans(String token) {

        try {
            Manager manager = tokenService.getManagerFromToken(token);

            List<Account> accounts = accountRepository.findByAgencyId(manager.getAgency().getId());

            if (accounts.isEmpty()) {
                throw new AccountNotFoundException("A agência ainda não possui contas cadastradas.");
            }

            List<Loan> loans = loanRepository.findByAccountIn(accounts);

            if (loans.isEmpty()) {
                throw new LoanProcessingException("Nenhum pedido de empréstimo registrado");
            }

            return loans.stream()
                    .map(LoanDto::new)
                    .toList();

        } catch (AccountNotFoundException | LoanProcessingException e) {
            throw e;
        } catch (Exception e) {
            throw new UnauthorizedAccessException("Erro ao processar o token ou verificar permissões. " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoanDto> pendingLoanApplications(String token) {

        try {
            Manager manager = tokenService.getManagerFromToken(token);

            List<Account> accounts = accountRepository.findByAgencyId(manager.getAgency().getId());

            if (accounts.isEmpty()) {
                throw new AccountNotFoundException("A agência ainda não possui contas cadastradas.");
            }

            List<Loan> loans = loanRepository.findByAccountInAndStatus(accounts, LoanStatus.PENDENTE);

            if (loans.isEmpty()) {
                throw new LoanProcessingException("Nenhum pedido de empréstimo PENDENTE registrado");
            }

            return loans.stream()
                    .map(LoanDto::new)
                    .toList();

        } catch (AccountNotFoundException | LoanProcessingException e) {
            throw e;
        } catch (Exception e) {
            throw new UnauthorizedAccessException("Erro ao processar o token ou verificar permissões. " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoanDto> canceledLoanApplications(String token) {

        try {
            Manager manager = tokenService.getManagerFromToken(token);

            List<Account> accounts = accountRepository.findByAgencyId(manager.getAgency().getId());

            if (accounts.isEmpty()) {
                throw new AccountNotFoundException("A agência ainda não possui contas cadastradas.");
            }

            List<Loan> loans = loanRepository.findByAccountInAndStatus(accounts, LoanStatus.CANCELADO);

            if (loans.isEmpty()) {
                throw new LoanProcessingException("Nenhum pedido de empréstimo CANCELADO registrado");
            }

            return loans.stream()
                    .map(LoanDto::new)
                    .toList();

        } catch (AccountNotFoundException | LoanProcessingException e) {
            throw e;
        } catch (Exception e) {
            throw new UnauthorizedAccessException("Erro ao processar o token ou verificar permissões. " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoanDto> approvedLoans(String token) {

        try {
            Manager manager = tokenService.getManagerFromToken(token);

            List<Account> accounts = accountRepository.findByAgencyId(manager.getAgency().getId());

            if (accounts.isEmpty()) {
                throw new AccountNotFoundException("A agência ainda não possui contas cadastradas.");
            }

            List<Loan> loans = loanRepository.findByAccountInAndStatus(accounts, LoanStatus.APROVADO);

            if (loans.isEmpty()) {
                throw new LoanProcessingException("Nenhum pedido de empréstimo APROVADO registrado");
            }

            return loans.stream()
                    .map(LoanDto::new)
                    .toList();

        } catch (AccountNotFoundException | LoanProcessingException e) {
            throw e;
        } catch (Exception e) {
            throw new UnauthorizedAccessException("Erro ao processar o token ou verificar permissões. " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void approveLoan(String token, ApprovalRequest request) {

        try {
            Manager manager = tokenService.getManagerFromToken(token);

            Loan requestLoan = loanRepository.findById(request.id())
                    .orElseThrow(() -> new LoanProcessingException("Pedido de empréstimo não encontrado."));

            Account account = accountRepository.findById(requestLoan.getAccount().getId())
                    .orElseThrow(() -> new AccountNotFoundException("Conta do cliente não encontrada."));

            if (!account.getAgency().getId().equals(manager.getAgency().getId())) {
                throw new UnauthorizedAccessException("Você não tem permissão para acessar esta conta.");
            }

            if (requestLoan.getStatus() == LoanStatus.PENDENTE) {

                if (request.approve()) {
                    requestLoan.setStatus(LoanStatus.APROVADO);
                    account.credit(requestLoan.getAmount());
                    accountRepository.save(account);
                } else {
                    requestLoan.setStatus(LoanStatus.CANCELADO);
                }

                loanRepository.save(requestLoan);
            } else {
                throw new UnauthorizedAccessException("Operação inválida: somente empréstimos com status PENDENTE podem ser atualizados.");
            }
        } catch(LoanProcessingException | AccountNotFoundException e) {
            throw e;
        } catch(Exception e) {
            throw new UnauthorizedAccessException("Erro ao processar o token ou verificar permissões. " + e.getMessage());
        }
    }
}
