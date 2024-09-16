package com.banking.quantum.client.domain.service.impl;

import com.banking.quantum.client.domain.account.Account;
import com.banking.quantum.client.domain.client.Client;
import com.banking.quantum.client.domain.dto.ExternalTransferDto;
import com.banking.quantum.client.domain.dto.InternalTransferDto;
import com.banking.quantum.client.domain.dto.PixTransferDto;
import com.banking.quantum.client.domain.dto.TransferOperationDto;
import com.banking.quantum.client.domain.repository.*;
import com.banking.quantum.client.domain.service.BankTransferService;
import com.banking.quantum.client.domain.service.validate.ValidateBankTransfer;
import com.banking.quantum.client.domain.transaction.ExternalTransfer;
import com.banking.quantum.client.domain.transaction.InternalTransfer;
import com.banking.quantum.client.domain.transaction.PixTransfer;
import com.banking.quantum.client.domain.transaction.TransactionType;
import com.banking.quantum.common.infra.exception.TransactionNotAuthorizedException;
import com.banking.quantum.common.infra.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
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
            transfer.setAccountType(operationDto.accountType());
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
            pix.setPixType(operationDto.pixType());
            pix.setPixKey(operationDto.pixKey());
            pix.setAmount(operationDto.amount());
            pix.setType(TransactionType.PIX);
            pix.setCreatedAt(LocalDateTime.now());

            validateBankTransfer.validatePixTransfer(pix, accountOrigin);

            if (client.isPresent()) {

                Account accountDestiny = client.get().getAccount();
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
