package com.banking.quantum.manager.domain.banking;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "Banking")
@Table(name = "tb_banking")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Banking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "bank_code", length = 3, unique = true)
    private String bankCode;
}