package com.banking.quantum.client.service;

import com.banking.quantum.client.domain.dto.client.ClientDto;
import com.banking.quantum.common.domain.dto.UserUpdateDto;

public interface ClientUpdateService {

    ClientDto updateClient(String token, UserUpdateDto updateDto);
}