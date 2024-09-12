package com.banking.quantum.client.domain.service.validate;

import com.banking.quantum.client.domain.client.Client;
import com.banking.quantum.client.domain.dto.CreateClientDto;
import com.banking.quantum.client.domain.repository.ClientRepository;
import com.banking.quantum.common.infra.exception.UserAlreadyExistsException;
import com.banking.quantum.common.infra.exception.ValidateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ValidateClient {

    @Autowired
    private ClientRepository clientRepository;

    public void validate(CreateClientDto clientDto) {

        if (clientDto.name() == null || clientDto.name().trim().isEmpty()) {
            throw new ValidateException("O nome é obrigatório.");
        }

        if(clientDto.cpf() == null || clientDto.cpf().trim().isEmpty()) {
            throw new ValidateException("O cpf é obrigatório.");

        } else if (!clientDto.cpf().matches("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}")) {
            throw new ValidateException("O CPF deve estar no formato XXX.XXX.XXX-XX.");
        }

        if(clientDto.phone() == null || clientDto.phone().trim().isEmpty()) {
            throw new ValidateException("O phone é obrigatório.");

        } else if (!clientDto.phone().matches("\\(\\d{2}\\) \\d{5}-\\d{4}")) {
            throw new ValidateException("O telefone deve estar no formato (XX) XXXXX-XXXX.");
        }

        if(clientDto.email() == null || clientDto.email().trim().isEmpty()) {
            throw new ValidateException("O email é obrigatório.");
        }

        if (!clientDto.password().matches(".{8,}")) {
            throw new ValidateException("A senha deve conter pelo menos 8 caracteres.");
        }

        Optional<Client> existingClient = clientRepository
                .findByCpfOrEmailOrPhone(clientDto.cpf(), clientDto.email(), clientDto.phone());

        existingClient.ifPresent(client -> {
            throw new UserAlreadyExistsException("O CPF, e-mail ou telefone fornecido já está registrado.");
        });

        if (clientDto.address().getLogradouro() == null || clientDto.address().getLogradouro().trim().isEmpty() ||
                clientDto.address().getNumero() == null || clientDto.address().getNumero().trim().isEmpty() ||
                clientDto.address().getBairro() == null || clientDto.address().getBairro().trim().isEmpty() ||
                clientDto.address().getCidade() == null || clientDto.address().getCidade().trim().isEmpty() ||
                clientDto.address().getCep() == null || clientDto.address().getCep().trim().isEmpty()) {
            throw new ValidateException("Os campos: Logradouro, número, bairro, cidade e CEP são obrigatórios.");
        }
    }
}