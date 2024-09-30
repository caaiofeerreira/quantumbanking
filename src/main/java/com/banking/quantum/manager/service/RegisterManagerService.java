package com.banking.quantum.manager.service;

import com.banking.quantum.manager.domain.dto.CreateManagerDto;

public interface RegisterManagerService {

    void registerManager(CreateManagerDto managerDto);
}