package com.banking.quantum.manager.domain.dto;

import com.banking.quantum.common.domain.address.Address;
import com.banking.quantum.manager.domain.banking.Agency;
import com.banking.quantum.manager.domain.banking.Banking;

public record AgencyDto(Long id,
                        String number,
                        String phone,
                        Address address,
                        Banking banking) {

    public AgencyDto(Agency agency) {
        this(agency.getId(),
                agency.getNumber(),
                agency.getPhone(),
                agency.getAddress(),
                agency.getBanking());
    }
}