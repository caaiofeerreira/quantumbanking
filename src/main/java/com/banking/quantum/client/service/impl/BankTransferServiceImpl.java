package com.banking.quantum.client.service.impl;

import com.banking.quantum.client.domain.account.Account;
import com.banking.quantum.client.domain.account.AccountStatus;
import com.banking.quantum.client.domain.client.Client;
import com.banking.quantum.client.domain.dto.transaction.ExternalTransferDto;
import com.banking.quantum.client.domain.dto.transaction.InternalTransferDto;
import com.banking.quantum.client.domain.dto.transaction.PixTransferDto;
import com.banking.quantum.client.domain.dto.transaction.TransferOperationDto;
import com.banking.quantum.client.repository.*;
import com.banking.quantum.client.service.BankTransferService;
import com.banking.quantum.client.service.validate.ValidateBankTransfer;
import com.banking.quantum.client.domain.transaction.ExternalTransfer;
import com.banking.quantum.client.domain.transaction.InternalTransfer;
import com.banking.quantum.client.domain.transaction.PixTransfer;
import com.banking.quantum.client.domain.transaction.TransactionType;
import com.banking.quantum.common.infra.exception.PixProcessingException;
import com.banking.quantum.common.infra.exception.TransactionNotAuthorizedException;
import com.banking.quantum.common.infra.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class BankTransferServiceImpl implements BankTransferService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ExternalTransferRepository externalTransferRepository;

    @Autowired
    private InternalTransferRepository internalTransferRepository;

    @Autowired
    private PixTransferRepository pixTransferRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ValidateBankTransfer validateBankTransfer;

    @Override
    @Transactional
    public InternalTransferDto processInternalTransfer(String token, TransferOperationDto operationDto) {

        try {
            Account accountOrigin = tokenService.getAccountFromToken(token);

            Account accountDestiny = accountRepository.findByAccountNumber(operationDto.accountDestiny())
                    .orElseThrow(() -> new TransactionNotAuthorizedException("Conta de destino não encontrada."));

            InternalTransfer transfer = new InternalTransfer();
            transfer.setAccountOrigin(accountOrigin);
            transfer.setAccountDestiny(accountDestiny);
            transfer.setAmount(operationDto.amount());
            transfer.setType(TransactionType.TRANSFER);
            transfer.setCreatedAt(LocalDateTime.now());

            validateBankTransfer.validateInternalTransfer(transfer, accountOrigin, accountDestiny);

            accountOrigin.debit(operationDto.amount());
            accountDestiny.credit(operationDto.amount());

            accountRepository.save(accountOrigin);
            accountRepository.save(accountDestiny);

            InternalTransfer newTransfer = internalTransferRepository.save(transfer);

            return new InternalTransferDto(newTransfer);

        } catch (Exception e) {
            throw new TransactionNotAuthorizedException("Transação não autorizada. " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ExternalTransferDto processExternalTransfer(String token, TransferOperationDto operationDto) {

        try {
            Account accountOrigin = tokenService.getAccountFromToken(token);

            ExternalTransfer transfer = new ExternalTransfer();
            transfer.setAccountOrigin(accountOrigin);
            transfer.setName(operationDto.name());
            transfer.setAccountDestiny(operationDto.accountDestiny());
            transfer.setAmount(operationDto.amount());
            transfer.setType(TransactionType.TRANSFER);
            transfer.setAccountType(operationDto.accountType().toUpperCase());
            transfer.setAgencyNumber(operationDto.agencyNumber());
            transfer.setBankCode(operationDto.bankCode());
            transfer.setDocument(operationDto.document());
            transfer.setCreatedAt(LocalDateTime.now());

            validateBankTransfer.validateExternalTransfer(transfer, accountOrigin);

            accountOrigin.debit(operationDto.amount());

            accountRepository.save(accountOrigin);

            ExternalTransfer newTransfer = externalTransferRepository.save(transfer);

            return new ExternalTransferDto(newTransfer);

        } catch (Exception e) {
            throw new TransactionNotAuthorizedException("Transação não autorizada. " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public PixTransferDto processPixTransfer(String token, TransferOperationDto operationDto) {

        try {
            Account accountOrigin = tokenService.getAccountFromToken(token);

            Optional<Client> client = clientRepository.findByPixKey(operationDto.pixKey());

            PixTransfer pix = new PixTransfer();
            pix.setAccountOrigin(accountOrigin);
            pix.setPixType(operationDto.pixType().toUpperCase());

            String formattedPixKey = operationDto.pixKey().replaceAll("\\s+", "").trim();
            pix.setPixKey(formattedPixKey);

            pix.setAmount(operationDto.amount());
            pix.setType(TransactionType.PIX);
            pix.setCreatedAt(LocalDateTime.now());

            validateBankTransfer.validatePixTransfer(pix, accountOrigin);

            if (client.isPresent()) {

                Account accountDestiny = client.get().getAccount();

                if (accountDestiny.getStatus() == AccountStatus.DESATIVADA || accountDestiny.getStatus() == AccountStatus.ENCERRADA) {
                    throw new PixProcessingException("A conta informada não está ativa (desativada ou encerrada). Verifique e tente novamente.");
                }

                pix.setAccountDestiny(accountDestiny);

                accountOrigin.debit(operationDto.amount());
                accountDestiny.credit(operationDto.amount());

                accountRepository.save(accountOrigin);
                accountRepository.save(accountDestiny);

            } else {
                pix.setAccountDestiny(null);
                accountOrigin.debit(operationDto.amount());
                accountRepository.save(accountOrigin);
            }

            PixTransfer newPix = pixTransferRepository.save(pix);

            return new PixTransferDto(newPix);

        } catch (Exception e) {
            throw new TransactionNotAuthorizedException("Transação não autorizada. " + e.getMessage());
        }
    }
}
