package com.banking.quantum.manager.repository;

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

//    @Query("SELECT m FROM Manager m WHERE m.cpf = :cpf OR m.email = :email OR m.phone = :phone")
//    Optional<Manager> findByCpfOrEmailOrPhone(@Param("cpf") String cpf,
//                                              @Param("email") String email,
//                                              @Param("phone") String phone);
//
//    @Query("SELECT m FROM Manager m WHERE m.email = :email OR m.phone = :phone")
//    Optional<Manager> findByEmailOrPhone(@Param("email") String email,
//                                         @Param("phone") String phone);

}