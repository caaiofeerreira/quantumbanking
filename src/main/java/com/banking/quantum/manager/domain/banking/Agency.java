package com.banking.quantum.manager.domain.banking;

import com.banking.quantum.client.domain.account.Account;
import com.banking.quantum.common.domain.address.Address;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity(name = "Agency")
@Table(name = "tb_agency")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Agency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "number")
    private String number;

    @Column(name = "phone")
    private String phone;

    @Embedded
    private Address address;

    @JsonManagedReference
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "agency")
    private List<Account> accountList;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "banking_id")
    private Banking banking;

}