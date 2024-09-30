package com.banking.quantum.client.service.validate;

import com.banking.quantum.client.domain.account.Account;
import com.banking.quantum.client.domain.client.Client;
import com.banking.quantum.client.domain.dto.CreateClientAccountDto;
import com.banking.quantum.client.domain.repository.AccountRepository;
import com.banking.quantum.client.domain.repository.ClientRepository;
import com.banking.quantum.common.infra.exception.AccountAlreadyExistsException;
import com.banking.quantum.common.infra.exception.UserAlreadyExistsException;
import com.banking.quantum.common.infra.exception.ValidateException;
import com.banking.quantum.manager.domain.repository.AgencyRepository;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ValidateClientAccount {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AgencyRepository agencyRepository;

    public void validate(CreateClientAccountDto clientAccountDto) {

        if (clientAccountDto.name() == null || clientAccountDto.name().trim().isEmpty()) {
            throw new ValidateException("O nome é obrigatório.");
        }

        if(clientAccountDto.cpf() == null || clientAccountDto.cpf().trim().isEmpty()) {
            throw new ValidateException("O cpf é obrigatório.");

        } else if (!clientAccountDto.cpf().matches("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}")) {
            throw new ValidateException("O CPF deve estar no formato XXX.XXX.XXX-XX.");
        }

        if (clientAccountDto.phone() == null || clientAccountDto.phone().trim().isEmpty()) {
            throw new ValidateException("O phone é obrigatório.");

        } else if (!clientAccountDto.phone().matches("\\(\\d{2}\\) \\d{5}-\\d{4}")) {
            throw new ValidateException("O telefone deve estar no formato (XX) XXXXX-XXXX.");
        }

        if (clientAccountDto.email() == null || clientAccountDto.email().trim().isEmpty()) {
            throw new ValidateException("O email é obrigatório.");
        }

        if (!isEmailValid(clientAccountDto.email())) {
            throw new ValidateException("O e-mail fornecido não é válido.");
        }

        if (!clientAccountDto.password().matches(".{8,}")) {
            throw new ValidateException("A senha deve conter pelo menos 8 caracteres.");
        }

        Optional<Client> existingClient = clientRepository
                .findByCpfOrEmailOrPhone(clientAccountDto.cpf(), clientAccountDto.email(), clientAccountDto.phone());

        existingClient.ifPresent(client -> {
            throw new UserAlreadyExistsException("O CPF, e-mail ou telefone fornecido já está registrado.");
        });

        if (clientAccountDto.address().getLogradouro() == null || clientAccountDto.address().getLogradouro().trim().isEmpty() ||
                clientAccountDto.address().getNumero() == null || clientAccountDto.address().getNumero().trim().isEmpty() ||
                clientAccountDto.address().getBairro() == null || clientAccountDto.address().getBairro().trim().isEmpty() ||
                clientAccountDto.address().getCidade() == null || clientAccountDto.address().getCidade().trim().isEmpty() ||
                clientAccountDto.address().getCep() == null || clientAccountDto.address().getCep().trim().isEmpty()) {

            throw new ValidateException("Os campos: Logradouro, número, bairro, cidade e CEP são obrigatórios.");
        }



        if (clientAccountDto.accountNumber() == null || clientAccountDto.accountNumber().trim().isEmpty()) {
            throw new ValidateException("O numero da conta é obrigatório.");

        } else if (!clientAccountDto.accountNumber().matches("\\d{5}-\\d")) {
            throw new ValidateException("O numero da conta deve estar no formato XXXXX-X");
        }

        if (clientAccountDto.agencyNumber() == null || clientAccountDto.agencyNumber().trim().isEmpty()) {
            List<String> registeredAgencies = agencyRepository.findAllAgencyNumbersAndCidade();
            String registeredAgenciesList = String.join(", ", registeredAgencies);

            throw new ValidateException("O número da agência é obrigatório. Agências registradas: " + registeredAgenciesList);
        }

        Optional<Account> existingAccount = accountRepository.findByAccountNumber(clientAccountDto.accountNumber());

        if(existingAccount.isPresent()) {
            throw new AccountAlreadyExistsException("O número de conta fornecido já está associado a outra conta.");
        }
    }

    private boolean isEmailValid(String email) {
        EmailValidator validator = EmailValidator.getInstance();
        return validator.isValid(email);
    }
}
