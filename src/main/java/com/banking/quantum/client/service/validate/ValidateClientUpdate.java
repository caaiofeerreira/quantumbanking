package com.banking.quantum.client.service.validate;

import com.banking.quantum.client.domain.client.Client;
import com.banking.quantum.client.domain.repository.ClientRepository;
import com.banking.quantum.common.infra.exception.UserAlreadyExistsException;
import com.banking.quantum.common.infra.exception.ValidateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ValidateClientUpdate {

    @Autowired
    private ClientRepository clientRepository;

    public void validate(Client clientUpdate) {

        Optional<Client> existingClient = clientRepository
                .findByEmailOrPhone(clientUpdate.getEmail(), clientUpdate.getPhone());

        existingClient.ifPresent(client -> {
            if (!client.getId().equals(clientUpdate.getId())) {
                throw new UserAlreadyExistsException("O dado fornecido já está em uso. Por favor, forneça um dado diferente.");
            }
        });

        if (clientUpdate.getAddress().getLogradouro() == null || clientUpdate.getAddress().getLogradouro().trim().isEmpty() ||
                clientUpdate.getAddress().getNumero() == null || clientUpdate.getAddress().getNumero().trim().isEmpty() ||
                clientUpdate.getAddress().getBairro() == null || clientUpdate.getAddress().getBairro().trim().isEmpty() ||
                clientUpdate.getAddress().getCidade() == null || clientUpdate.getAddress().getCidade().trim().isEmpty() ||
                clientUpdate.getAddress().getEstado() == null || clientUpdate.getAddress().getEstado().trim().isEmpty() ||
                clientUpdate.getAddress().getCep() == null || clientUpdate.getAddress().getCep().trim().isEmpty()) {
            throw new ValidateException("Os campos: logradouro, número, bairro, cidade, estado e cep são obrigatórios. Apenas o campo complemento não é obrigatório.");
        }
    }
}