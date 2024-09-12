package com.banking.quantum.manager.domain.dto;

import com.banking.quantum.common.domain.address.Address;
import jakarta.validation.constraints.*;

public record CreateManagerDto(String name,
                               String cpf,
                               String email,
                               String phone,
                               String password,
                               String agencyNumber,
                               Address address) {
}