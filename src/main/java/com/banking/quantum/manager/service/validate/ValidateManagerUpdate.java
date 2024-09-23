package com.banking.quantum.manager.service.validate;

import com.banking.quantum.common.infra.exception.UserAlreadyExistsException;
import com.banking.quantum.common.infra.exception.ValidateException;
import com.banking.quantum.manager.domain.manager.Manager;
import com.banking.quantum.manager.repository.ManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ValidateManagerUpdate {

    @Autowired
    private ManagerRepository managerRepository;

    public void validate(Manager managerUpdate) {

        Optional<Manager> existingManager = managerRepository
                .findByEmailOrPhone(managerUpdate.getEmail(), managerUpdate.getPhone());

        existingManager.ifPresent(manager -> {
            if (!manager.getId().equals(managerUpdate.getId())) {
                throw new UserAlreadyExistsException("O dado fornecido já está em uso. Por favor, forneça um dado diferente.");
            }
        });

        if (managerUpdate.getAddress().getLogradouro() == null || managerUpdate.getAddress().getLogradouro().trim().isEmpty() ||
                managerUpdate.getAddress().getNumero() == null || managerUpdate.getAddress().getNumero().trim().isEmpty() ||
                managerUpdate.getAddress().getBairro() == null || managerUpdate.getAddress().getBairro().trim().isEmpty() ||
                managerUpdate.getAddress().getCidade() == null || managerUpdate.getAddress().getCidade().trim().isEmpty() ||
                managerUpdate.getAddress().getEstado() == null || managerUpdate.getAddress().getEstado().trim().isEmpty() ||
                managerUpdate.getAddress().getCep() == null || managerUpdate.getAddress().getCep().trim().isEmpty()) {

            throw new ValidateException("Os campos: logradouro, número, bairro, cidade, estado e cep são obrigatórios. Apenas o campo complemento não é obrigatório.");
        }
    }
}