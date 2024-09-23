package com.banking.quantum.manager.service;

import com.banking.quantum.common.domain.address.Address;
import com.banking.quantum.common.domain.dto.UserUpdateDto;
import com.banking.quantum.common.infra.exception.UnauthorizedAccessException;
import com.banking.quantum.common.infra.exception.ValidateException;
import com.banking.quantum.common.infra.security.TokenService;
import com.banking.quantum.manager.domain.dto.ManagerDto;
import com.banking.quantum.manager.domain.manager.Manager;
import com.banking.quantum.manager.repository.ManagerRepository;
import com.banking.quantum.manager.service.validate.ValidateManagerUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ManagerUpdateService {

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private ValidateManagerUpdate validateManagerUpdate;

    @Autowired
    private TokenService tokenService;

    @Transactional
    public ManagerDto managerUpdate(String token, UserUpdateDto updateDto) {

        try {

            Manager manager = tokenService.getManagerFromToken(token);

            Address address = updateDto.address();

            if (address != null) {

                if (address.getLogradouro() != null)
                    manager.getAddress().setLogradouro(address.getLogradouro());
                if (address.getNumero() != null)
                    manager.getAddress().setNumero(address.getNumero());
                if (address.getBairro() != null)
                    manager.getAddress().setBairro(address.getBairro());
                if (address.getCidade() != null)
                    manager.getAddress().setCidade(address.getCidade());
                if (address.getEstado() != null)
                    manager.getAddress().setEstado(address.getEstado());
                if (address.getCep() != null)
                    manager.getAddress().setCep(address.getCep());

                manager.getAddress().setComplemento(address.getComplemento());
            }

            validateManagerUpdate.validate(manager);

            managerRepository.save(manager);

            return new ManagerDto(manager);

        } catch (ValidateException e) {
            throw e;
        } catch (Exception e) {
            throw new UnauthorizedAccessException("Ocorreu um erro ao processar sua solicitação. " +
                    "Por favor, verifique os dados e tente novamente.");
        }
    }
}