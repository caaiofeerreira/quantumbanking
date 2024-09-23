package com.banking.quantum.client.domain.transaction;

import com.banking.quantum.client.domain.account.Account;
import com.banking.quantum.client.domain.account.AccountType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "ExternalTransfer")
@Table(name = "tb_external_transfer")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class ExternalTransfer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "account_origin")
    private Account accountOrigin;

    @Column(name = "account_destiny")
    private String accountDestiny;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Column(name = "agency_number")
    private String agencyNumber;

    @Column(name = "bank_code")
    private String bankCode;

    private String document;

    @Column(name = "account_type")
    private String accountType;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

}