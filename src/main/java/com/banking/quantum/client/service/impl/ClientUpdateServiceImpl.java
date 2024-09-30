package com.banking.quantum.client.service.impl;

import com.banking.quantum.client.domain.account.Account;
import com.banking.quantum.client.domain.dto.client.ClientDto;
import com.banking.quantum.client.domain.repository.ClientRepository;
import com.banking.quantum.client.service.ClientUpdateService;
import com.banking.quantum.client.service.validate.ValidateClientUpdate;
import com.banking.quantum.common.domain.address.Address;
import com.banking.quantum.common.domain.dto.UserUpdateDto;
import com.banking.quantum.common.infra.exception.UnauthorizedAccessException;
import com.banking.quantum.common.infra.exception.ValidateException;
import com.banking.quantum.common.infra.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClientUpdateServiceImpl implements ClientUpdateService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ValidateClientUpdate validateClientUpdate;

    @Autowired
    private TokenService tokenService;

    @Override
    @Transactional
    public ClientDto updateClient(String token, UserUpdateDto updateDto) {

        try {

            Account account = tokenService.getAccountFromToken(token);

            Address address = updateDto.address();

            if (address != null) {

                if (address.getLogradouro() != null)
                    account.getClient().getAddress().setLogradouro(address.getLogradouro());
                if (address.getNumero() != null)
                    account.getClient().getAddress().setNumero(address.getNumero());
                if (address.getBairro() != null)
                    account.getClient().getAddress().setBairro(address.getBairro());
                if (address.getCidade() != null)
                    account.getClient().getAddress().setCidade(address.getCidade());
                if (address.getEstado() != null)
                    account.getClient().getAddress().setEstado(address.getEstado());
                if (address.getCep() != null)
                    account.getClient().getAddress().setCep(address.getCep());

                account.getClient().getAddress().setComplemento(address.getComplemento());
            }

            validateClientUpdate.validate(account.getClient());

            clientRepository.save(account.getClient());

            return new ClientDto(account.getClient());

        } catch (ValidateException e) {
            throw e;
        } catch (Exception e) {
            throw new UnauthorizedAccessException("Ocorreu um erro ao processar sua solicitação. " +
                    "Por favor, verifique os dados e tente novamente.");
        }
    }
}