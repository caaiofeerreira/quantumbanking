package com.banking.quantum.client.domain.transaction;

import com.banking.quantum.client.domain.account.Account;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "PixTransfer")
@Table(name = "tb_pix_transfer")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class PixTransfer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "account_origin")
    private Account accountOrigin;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "account_destiny", nullable = true)
    private Account accountDestiny;

    @Column(name = "pix_type")
    @Enumerated(EnumType.STRING)
    private PixType pixType;

    @Column(name = "pix_key")
    private String pixKey;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}