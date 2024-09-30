package com.banking.quantum.manager.service;

import com.banking.quantum.common.domain.dto.UserUpdateDto;
import com.banking.quantum.manager.domain.dto.ManagerDto;

public interface ManagerUpdateService {

    ManagerDto managerUpdate(String token, UserUpdateDto updateDto);
}