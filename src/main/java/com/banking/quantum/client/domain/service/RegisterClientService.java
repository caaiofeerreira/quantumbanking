package com.banking.quantum.client.domain.service;

import com.banking.quantum.client.domain.account.Account;
import com.banking.quantum.client.domain.account.AccountStatus;
import com.banking.quantum.client.domain.account.AccountType;
import com.banking.quantum.client.domain.client.Client;
import com.banking.quantum.client.domain.client.ClientType;
import com.banking.quantum.client.domain.dto.ClientDto;
import com.banking.quantum.client.domain.dto.CreateAccountDto;
import com.banking.quantum.client.domain.dto.CreateClientDto;
import com.banking.quantum.client.domain.repository.AccountRepository;
import com.banking.quantum.client.domain.repository.ClientRepository;
import com.banking.quantum.client.domain.service.validate.ValidateAccount;
import com.banking.quantum.client.domain.service.validate.ValidateClient;
import com.banking.quantum.common.domain.user.UserRole;
import com.banking.quantum.common.infra.exception.ClientAccountCreationException;
import com.banking.quantum.common.infra.exception.ValidateException;
import com.banking.quantum.manager.domain.banking.Agency;
import com.banking.quantum.manager.domain.repository.AgencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class RegisterClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AgencyRepository agencyRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ValidateClient validateClient;

    @Autowired
    private ValidateAccount validateAccount;

    @Transactional
    public ClientDto registerClient(CreateClientDto clientDto, CreateAccountDto accountDto) {

        try {
            validateClient.validate(clientDto);
            validateAccount.validate(accountDto);

            String password = passwordEncoder.encode(clientDto.password());

            Client client = new Client();

            client.setName(clientDto.name());
            client.setCpf(clientDto.cpf());
            client.setPhone(clientDto.phone());
            client.setEmail(clientDto.email());
            client.setPassword(password);
            client.setUserRole(UserRole.CLIENT);
            client.setAddress(clientDto.address());
            client.setType(clientDto.type());

            Account account = new Account();

            account.setAccountNumber(accountDto.accountNumber());
            account.setType(accountDto.type());
            account.setStatus(AccountStatus.ACTIVE);
            account.setBalance(BigDecimal.ZERO);

            if (client.getType() == ClientType.FISICA) {
               account.setType(AccountType.CORRENTE);
            } else if (client.getType() == ClientType.JURIDICA) {
                account.setType(AccountType.JURIDICA);
            }

            Optional<Agency> agency = agencyRepository.findByNumber(accountDto.agencyNumber());

            if (agency.isPresent()) {
                account.setAgency(agency.get());
            } else {
                List<String> registeredAgencies = agencyRepository.findAllAgencyNumbersAndCidade();
                String registeredAgenciesList = String.join(", ", registeredAgencies);

                throw new ValidateException("Agência não encontrada. Agências registradas: " + registeredAgenciesList);
            }

            Client newClient = clientRepository.save(client);
            account.setClient(newClient);
            Account newAccount = accountRepository.save(account);
            client.setAccount(newAccount);

            return new ClientDto(newClient);

        } catch (Exception e) {
            throw new ClientAccountCreationException("Dados inválidos. Por favor, verifique as informações e tente novamente. " + e.getMessage());
        }
    }
}