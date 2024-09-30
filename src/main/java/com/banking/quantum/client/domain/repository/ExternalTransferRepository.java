package com.banking.quantum.client.domain.repository;

import com.banking.quantum.client.domain.transaction.ExternalTransfer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ExternalTransferRepository extends JpaRepository<ExternalTransfer, UUID> {

}
