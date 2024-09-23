package com.banking.quantum.client.domain.account;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity(name = "CloseAccount")
@Table(name = "tb_close_account")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class CloseAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_status")
    private AccountCloseStatus status;

    @Column(name = "closing_date")
    private LocalDateTime closingDate;
}