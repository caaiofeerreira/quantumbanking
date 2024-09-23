package com.banking.quantum.client.service.validate;

import com.banking.quantum.client.domain.account.Account;
import com.banking.quantum.client.domain.dto.account.CreateAccountDto;
import com.banking.quantum.client.repository.AccountRepository;
import com.banking.quantum.common.infra.exception.AccountAlreadyExistsException;
import com.banking.quantum.common.infra.exception.ValidateException;
import com.banking.quantum.manager.repository.AgencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ValidateAccount {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AgencyRepository agencyRepository;

    public void validate(CreateAccountDto accountDto) {

        if (accountDto.accountNumber() == null || accountDto.accountNumber().trim().isEmpty()) {
            throw new ValidateException("O numero da conta é obrigatório.");

        } else if (!accountDto.accountNumber().matches("\\d{5}-\\d")) {
            throw new ValidateException("O numero da conta deve estar no formato XXXXX-X");
        }

        if (accountDto.agencyNumber() == null || accountDto.agencyNumber().trim().isEmpty()) {
            List<String> registeredAgencies = agencyRepository.findAllAgencyNumbersAndCidade();
            String registeredAgenciesList = String.join(", ", registeredAgencies);

            throw new ValidateException("O número da agência é obrigatório. Agências registradas: " + registeredAgenciesList);
        }

        Optional<Account> existingAccount = accountRepository.findByAccountNumber(accountDto.accountNumber());

        if(existingAccount.isPresent()) {
            throw new AccountAlreadyExistsException("O número de conta fornecido já está associado a outra conta.");
        }
    }
}