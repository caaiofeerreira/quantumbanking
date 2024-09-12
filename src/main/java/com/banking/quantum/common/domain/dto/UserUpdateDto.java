package com.banking.quantum.common.domain.dto;

import com.banking.quantum.common.domain.address.Address;
import jakarta.validation.constraints.NotBlank;

public record UserUpdateDto(@NotBlank String email,
                            @NotBlank String phone,
                            Address address){
}