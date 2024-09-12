package com.banking.quantum.common.domain.dto;

import com.banking.quantum.common.domain.address.Address;
import jakarta.validation.constraints.NotBlank;

public record AddressDto(String logradouro,
                         String numero,
                         String complemento,
                         String bairro,
                         String cidade,
                         String estado,
                         String cep ) {

    public AddressDto(Address address) {
        this(address.getLogradouro(),
                address.getNumero(),
                address.getComplemento(),
                address.getBairro(),
                address.getCidade(),
                address.getEstado(),
                address.getCep());
    }
}