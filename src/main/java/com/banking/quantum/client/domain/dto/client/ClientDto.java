package com.banking.quantum.client.domain.dto.client;

import com.banking.quantum.client.domain.client.Client;
import com.banking.quantum.client.domain.client.ClientType;
import com.banking.quantum.common.domain.address.Address;

public record ClientDto(Long id,
                        String name,
                        String cpf,
                        String phone,
                        String email,
                        String pixKey,
                        Address address,
                        ClientType type) {

    public ClientDto(Client client) {
        this(client.getId(),
                client.getName(),
                client.getCpf(),
                client.getPhone(),
                client.getEmail(),
                client.getPixKey(),
                client.getAddress(),
                client.getType());
    }
}