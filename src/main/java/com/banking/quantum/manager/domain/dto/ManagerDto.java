package com.banking.quantum.manager.domain.dto;

import com.banking.quantum.common.domain.address.Address;
import com.banking.quantum.manager.domain.banking.Agency;
import com.banking.quantum.manager.domain.manager.Manager;

public record ManagerDto(Long id,
                         String name,
                         String cpf,
                         String phone,
                         String email,
                         Address address) {

    public ManagerDto(Manager manager) {
        this(manager.getId(),
                manager.getName(),
                manager.getCpf(),
                manager.getPhone(),
                manager.getEmail(),
                manager.getAddress());
    }
}
