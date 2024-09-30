package com.banking.quantum.client.domain.repository;

import com.banking.quantum.client.domain.transaction.PixTransfer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PixTransferRepository extends JpaRepository<PixTransfer, UUID> {

}