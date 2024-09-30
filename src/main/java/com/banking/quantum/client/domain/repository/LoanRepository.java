package com.banking.quantum.client.domain.repository;

import com.banking.quantum.client.domain.account.Account;
import com.banking.quantum.client.domain.loan.Loan;
import com.banking.quantum.client.domain.loan.LoanStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    List<Loan> findByAccountIn(List<Account> accounts);

    List<Loan> findByAccountInAndStatus(List<Account> accounts, LoanStatus status);

    List<Loan> findByAccountIdAndStatus(Long id, LoanStatus status);
}