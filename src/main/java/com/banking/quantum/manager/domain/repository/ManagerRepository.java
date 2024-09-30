package com.banking.quantum.manager.domain.repository;

import com.banking.quantum.manager.domain.manager.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ManagerRepository extends JpaRepository<Manager, Long> {

    Manager findByCpf(String cpf);

    Optional<Manager> findByCpfOrEmailOrPhone(String cpf,
                                              String email,
                                              String phone);

    Optional<Manager> findByEmailOrPhone(String email,
                                         String phone);
}