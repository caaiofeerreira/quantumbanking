package com.banking.quantum.manager.domain.service.validate;

import com.banking.quantum.common.infra.exception.*;
import com.banking.quantum.manager.domain.dto.CreateManagerDto;
import com.banking.quantum.manager.domain.manager.Manager;
import com.banking.quantum.manager.domain.repository.AgencyRepository;
import com.banking.quantum.manager.domain.repository.ManagerRepository;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ValidateManager {

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private AgencyRepository agencyRepository;

    public void validate(CreateManagerDto managerDto) {

        if (managerDto.name() == null || managerDto.name().trim().isEmpty()) {
            throw new ValidateException("O nome é obrigatório.");
        }

        if (managerDto.cpf() == null || managerDto.cpf().trim().isEmpty()) {
            throw new ValidateException("O cpf é obrigatório.");

        } else if (!managerDto.cpf().matches("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}")) {
            throw new ValidateException("O CPF deve estar no formato XXX.XXX.XXX-XX.");
        }

        if (managerDto.phone() == null || managerDto.phone().trim().isEmpty()) {
            throw new ValidateException("O phone é obrigatório.");

        } else if (!managerDto.phone().matches("\\(\\d{2}\\) \\d{5}-\\d{4}")) {
            throw new ValidateException("O telefone deve estar no formato (XX) XXXXX-XXXX.");
        }

        if (managerDto.email() == null || managerDto.email().trim().isEmpty()) {
            throw new ValidateException("O email é obrigatório.");
        }

        if (!isEmailValid(managerDto.email())) {
            throw new ValidateException("O e-mail fornecido não é válido.");
        }

        if (!managerDto.password().matches(".{8,}")) {
            throw new ValidateException("A senha deve conter pelo menos 8 caracteres.");
        }

        if (managerDto.agencyNumber() == null || managerDto.agencyNumber().trim().isEmpty()) {
            List<String> registeredAgencies = agencyRepository.findAllAgencyNumbersAndCidade();
            String registeredAgenciesList = String.join(", ", registeredAgencies);

            throw new ValidateException("O número da agência é obrigatório. Agências registradas: " + registeredAgenciesList);
        }

        Optional<Manager> existingManager = managerRepository
                .findByCpfOrEmailOrPhone(managerDto.cpf(), managerDto.email(), managerDto.phone());

        existingManager.ifPresent(manager -> {
            throw new UserAlreadyExistsException("O CPF, e-mail ou telefone fornecido já está registrado.");
        });

        if (managerDto.address().getLogradouro() == null || managerDto.address().getLogradouro().trim().isEmpty() ||
                managerDto.address().getNumero() == null || managerDto.address().getNumero().trim().isEmpty() ||
                managerDto.address().getBairro() == null || managerDto.address().getBairro().trim().isEmpty() ||
                managerDto.address().getCidade() == null || managerDto.address().getCidade().trim().isEmpty() ||
                managerDto.address().getCep() == null || managerDto.address().getCep().trim().isEmpty()) {

            throw new ValidateException("Os campos: Logradouro, número, bairro, cidade e CEP são obrigatórios.");
        }
    }

    private boolean isEmailValid(String email) {
        EmailValidator validator = EmailValidator.getInstance();
        return validator.isValid(email);
    }
}