package com.banking.quantum.client.domain.repository;

import com.banking.quantum.client.domain.transaction.InternalTransfer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InternalTransferRepository extends JpaRepository<InternalTransfer, UUID> {

}