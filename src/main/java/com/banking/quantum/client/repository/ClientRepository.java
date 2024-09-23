package com.banking.quantum.client.repository;

import com.banking.quantum.client.domain.client.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {

    Client findByCpf(String cpf);

    @Query("SELECT c FROM Client c WHERE c.cpf = :cpf OR c.email = :email OR c.phone = :phone")
    Optional<Client> findByCpfOrEmailOrPhone(@Param("cpf") String cpf,
                                             @Param("email") String email,
                                             @Param("phone") String phone);

    Client findByAccountId(Long id);

    Optional<Client> findByPixKey(String pixKey);

    @Query("SELECT c FROM Client c WHERE c.email = :email OR c.phone = :phone")
    Optional<Client> findByEmailOrPhone(@Param("email") String email,
                                        @Param("phone") String phone);
}