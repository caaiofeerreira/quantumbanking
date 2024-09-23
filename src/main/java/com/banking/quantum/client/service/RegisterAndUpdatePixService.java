package com.banking.quantum.client.service;

import com.banking.quantum.client.domain.dto.transaction.PixKeyDto;

public interface RegisterAndUpdatePixService {

    PixKeyDto registerKeyPix(String token, PixKeyDto pixKeyDto);

    PixKeyDto updatePixKey(String token, PixKeyDto pixKeyDto);
}