package com.banking.quantum.client.domain.account;

import com.banking.quantum.client.domain.client.Client;
import com.banking.quantum.common.infra.exception.TransactionNotAuthorizedException;
import com.banking.quantum.manager.domain.banking.Agency;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Entity(name = "Account")
@Table(name = "tb_account")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@EqualsAndHashCode(of = "id")
public class Account implements AccountOperation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "number", unique = true)
    private String accountNumber;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private AccountType type;

    @Column(name = "balance")
    private BigDecimal balance;

    @OneToOne
    @JsonBackReference
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne
    @JoinColumn(name = "agency_id")
    private Agency agency;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @JsonIgnore
    @Transient
    private final Lock lock = new ReentrantLock();

    @Override
    public void debit(BigDecimal amount) {

        lock.lock();

        try {
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new TransactionNotAuthorizedException("O valor deve ser positivo");
            }
            if (amount.compareTo(balance) > 0) {
                throw new TransactionNotAuthorizedException("Saldo insuficiente");
            }
            this.balance = this.balance.subtract(amount);

        } finally {
            lock.unlock();
        }
    }

    @Override
    public void credit(BigDecimal amount) {

        lock.lock();

        try {
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new TransactionNotAuthorizedException("O valor deve ser positivo");
            }
            this.balance = this.balance.add(amount);

        } finally {
            lock.unlock();
        }
    }
}