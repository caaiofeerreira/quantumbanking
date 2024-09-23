package com.banking.quantum.client.service.impl;

import com.banking.quantum.client.domain.client.Client;
import com.banking.quantum.client.domain.dto.client.ClientDto;
import com.banking.quantum.client.domain.dto.transaction.PixKeyDto;
import com.banking.quantum.client.repository.ClientRepository;
import com.banking.quantum.client.service.RegisterAndUpdatePixService;
import com.banking.quantum.client.service.validate.ValidateRegisterAndUpdatePix;
import com.banking.quantum.common.infra.exception.PixProcessingException;
import com.banking.quantum.common.infra.exception.UnauthorizedAccessException;
import com.banking.quantum.common.infra.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegisterAndUpdatePixServiceImpl implements RegisterAndUpdatePixService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ValidateRegisterAndUpdatePix validatePix;

    @Autowired
    private TokenService tokenService;

    @Override
    @Transactional
    public PixKeyDto registerKeyPix(String token, PixKeyDto pixKey) {

        try {
            Client client = tokenService.getAccountFromToken(token).getClient();

            validatePix.validateRegister(client, pixKey);

            client.setPixKey(pixKey.pixKey());
            clientRepository.save(client);

            return pixKey;
        } catch (PixProcessingException e) {
            throw e;
        }
    }

    @Override
    @Transactional
    public PixKeyDto updatePixKey(String token, PixKeyDto pixKey) {

        try {
            Client client = tokenService.getAccountFromToken(token).getClient();

            validatePix.validateUpdate(client, pixKey);

            client.setPixKey(pixKey.pixKey());
            clientRepository.save(client);

            return pixKey;
        } catch (PixProcessingException e) {
            throw e;
        }
    }

}