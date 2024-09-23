package com.banking.quantum.client.repository;

import com.banking.quantum.client.domain.transaction.AccountMovement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountMovementRepository extends JpaRepository<AccountMovement, UUID> {
}
