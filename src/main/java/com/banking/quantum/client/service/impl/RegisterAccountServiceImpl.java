package com.banking.quantum.client.service.impl;

import com.banking.quantum.client.domain.account.Account;
import com.banking.quantum.client.domain.account.AccountStatus;
import com.banking.quantum.client.domain.account.AccountType;
import com.banking.quantum.client.domain.client.Client;
import com.banking.quantum.client.domain.client.ClientType;
import com.banking.quantum.client.domain.dto.CreateClientAccountDto;
import com.banking.quantum.client.domain.dto.client.ClientDto;
import com.banking.quantum.client.domain.repository.AccountRepository;
import com.banking.quantum.client.domain.repository.ClientRepository;
import com.banking.quantum.client.service.RegisterAccountService;
import com.banking.quantum.client.service.validate.ValidateClientAccount;
import com.banking.quantum.common.domain.user.UserRole;
import com.banking.quantum.common.infra.exception.RegisterUserException;
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
public class RegisterAccountServiceImpl implements RegisterAccountService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AgencyRepository agencyRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ValidateClientAccount validateClientAccount;

    @Override
    @Transactional
    public void registerAccount(CreateClientAccountDto clientAccountDto) {

        try {
            validateClientAccount.validate(clientAccountDto);

            String password = passwordEncoder.encode(clientAccountDto.password());

            Client client = new Client();

            client.setName(clientAccountDto.name());
            client.setCpf(clientAccountDto.cpf());
            client.setPhone(clientAccountDto.phone().replace(" ", ""));
            client.setEmail(clientAccountDto.email());
            client.setPassword(password);
            client.setUserRole(UserRole.CLIENT);
            client.setAddress(clientAccountDto.address());
            client.setType(clientAccountDto.clientType());

            Account account = new Account();

            account.setAccountNumber(clientAccountDto.accountNumber());
            account.setType(clientAccountDto.accountType());
            account.setStatus(AccountStatus.ATIVA);
            account.setBalance(BigDecimal.ZERO);

            if (client.getType() == ClientType.FISICA) {
                account.setType(AccountType.CORRENTE);
            } else if (client.getType() == ClientType.JURIDICA) {
                account.setType(AccountType.JURIDICA);
            }

            Optional<Agency> agency = agencyRepository.findByNumber(clientAccountDto.agencyNumber());

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

            new ClientDto(newClient);

        } catch (Exception e) {
            throw new RegisterUserException("Dados inválidos. Por favor, verifique as informações e tente novamente. " + e.getMessage());
        }
    }
}