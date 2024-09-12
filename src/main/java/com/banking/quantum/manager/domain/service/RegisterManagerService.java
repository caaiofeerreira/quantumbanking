package com.banking.quantum.manager.domain.service;

import com.banking.quantum.common.domain.user.UserRole;
import com.banking.quantum.common.infra.exception.ManagerCreateExeption;
import com.banking.quantum.common.infra.exception.ValidateException;
import com.banking.quantum.manager.domain.banking.Agency;
import com.banking.quantum.manager.domain.dto.CreateManagerDto;
import com.banking.quantum.manager.domain.dto.ManagerDto;
import com.banking.quantum.manager.domain.manager.Manager;
import com.banking.quantum.manager.domain.repository.AgencyRepository;
import com.banking.quantum.manager.domain.repository.ManagerRepository;
import com.banking.quantum.manager.domain.service.validate.ValidateManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RegisterManagerService {

    @Autowired
    private AgencyRepository agencyRepository;

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ValidateManager validateManager;

    @Transactional
    public ManagerDto registerManager(CreateManagerDto managerDto) {

        try {
            validateManager.validate(managerDto);

            String passwordEncoded = passwordEncoder.encode(managerDto.password());

            Manager manager = new Manager();
            manager.setName(managerDto.name());
            manager.setCpf(managerDto.cpf());
            manager.setPhone(managerDto.phone());
            manager.setEmail(managerDto.email());
            manager.setPassword(passwordEncoded);
            manager.setAddress(managerDto.address());

            Optional<Agency> agency = agencyRepository.findByNumber(managerDto.agencyNumber());

            if (agency.isPresent()) {
                manager.setAgency(agency.get());
            } else {
                List<String> registeredAgencies = agencyRepository.findAllAgencyNumbersAndCidade();
                String registeredAgenciesList = String.join(", ", registeredAgencies);

                throw new ValidateException("Agência não encontrada. Agências registradas: " + registeredAgenciesList);
            }

            manager.setUserRole(UserRole.MANAGER);

            Manager newManager = managerRepository.save(manager);

            return new ManagerDto(newManager);

        } catch (Exception e) {
            throw new ManagerCreateExeption("Dados inválidos. Por favor, verifique as informações e tente novamente. " + e.getMessage());
        }
    }
}