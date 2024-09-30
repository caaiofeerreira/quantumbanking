package com.banking.quantum.client.service.validate;

import com.banking.quantum.client.domain.account.Account;
import com.banking.quantum.client.domain.account.AccountStatus;
import com.banking.quantum.client.domain.repository.*;
import com.banking.quantum.client.domain.transaction.ExternalTransfer;
import com.banking.quantum.client.domain.transaction.InternalTransfer;
import com.banking.quantum.client.domain.transaction.PixTransfer;
import com.banking.quantum.common.infra.exception.AccountNotFoundException;
import com.banking.quantum.common.infra.exception.TransactionNotAuthorizedException;
import com.banking.quantum.common.infra.exception.UnauthorizedAccessException;
import com.banking.quantum.common.infra.exception.ValidateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ValidateBankTransfer {

    @Autowired
    private ExternalTransferRepository externalTransferRepository;

    @Autowired
    private InternalTransferRepository internalTransferRepository;

    @Autowired
    private PixTransferRepository pixTransferRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    private static final BigDecimal MINIMUM_VALUE_TRANSACTION = new BigDecimal("10.00");

    public void validateInternalTransfer(InternalTransfer internalTransfer, Account accountOrigin, Account accountDestiny) {

        BigDecimal amount = internalTransfer.getAmount();
        String accountDestinyNumber = accountDestiny.getAccountNumber();

        validateCommon(accountOrigin, amount, accountDestinyNumber);

        if (accountDestiny.getStatus() == AccountStatus.DESATIVADA) {
            throw new AccountNotFoundException("A transação não pode ser realizada, pois esta conta foi desativada.");
        }

        if (accountOrigin.equals(internalTransfer.getAccountDestiny())) {
            throw new UnauthorizedAccessException("Não é possível transferir dinheiro para a mesma conta");
        }
    }

    public void validateExternalTransfer(ExternalTransfer externalTransfer, Account accountOrigin) {

        String accountDestiny = externalTransfer.getAccountDestiny();
        String document = externalTransfer.getDocument();
        BigDecimal amount = externalTransfer.getAmount();

        validateCommon(accountOrigin, amount, accountDestiny);

        if (accountOrigin.getAccountNumber().equals(accountDestiny)) {
            throw new TransactionNotAuthorizedException("Não é possível transferir dinheiro para a mesma conta");
        }

        if (document == null || !(document.matches("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}") ||
                        document.matches("\\d{2}\\.\\d{3}\\.\\d{3}-\\d") ||
                        document.matches("\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}"))) {

            throw new ValidateException("Formato do documento inválido. CPF: XXX.XXX.XXX-XX, RG: XX.XXX.XXX-X, CNPJ: XX.XXX.XXX/XXXX-XX");
        }

        if (externalTransfer.getName() == null || externalTransfer.getName().trim().isEmpty()) {
            throw new ValidateException("O campo 'name' não pode estar vazio.");
        }

        if (externalTransfer.getAccountDestiny() == null || externalTransfer.getAccountDestiny().trim().isEmpty()) {
            throw new ValidateException("O campo 'accountDestiny' não pode estar vazio.");
        }

        if (externalTransfer.getAgencyNumber() == null || externalTransfer.getAgencyNumber().trim().isEmpty()) {
            throw new ValidateException("O campo 'agencyNumber' não pode estar vazio.");
        }

        if (externalTransfer.getBankCode() == null || externalTransfer.getBankCode().trim().isEmpty()) {
            throw new ValidateException("O campo 'bankCode' não pode estar vazio.");
        }

        if (externalTransfer.getAccountType() == null || externalTransfer.getAccountType().trim().isEmpty()) {
            throw new ValidateException("O campo 'accountType' não pode estar vazio.");
        }

        if (!isValidAccountType(externalTransfer.getAccountType())) {
            throw new ValidateException("O valor fornecido para 'accountType' é inválido. Valores válidos: CORRENTE, POUPANCA, JURIDICA.");
        }
    }

    private boolean isValidAccountType(String accountType) {
        return "CORRENTE".equals(accountType) || "POUPANCA".equals(accountType) || "JURIDICA".equals(accountType);
    }

    private void validateCommon(Account accountOrigin, BigDecimal amount, String accountDestiny) {

        if (accountOrigin.getStatus() == AccountStatus.DESATIVADA) {
            throw new AccountNotFoundException("A operação não pode ser concluída porque sua conta está desativada. Por favor, entre em contato com o suporte para mais informações ou vá até sua agência para assistência adicional.");
        }

        if (!accountDestiny.matches("\\d{5}-\\d")) {
            throw new ValidateException("Formato do número da conta inválido. Formato: XXXXX-X");
        }

        if (amount == null) {
            throw new TransactionNotAuthorizedException("O valor da transferência não pode ser nulo.");
        }

        if (amount.compareTo(MINIMUM_VALUE_TRANSACTION) < 0) {
            throw new TransactionNotAuthorizedException("O valor minimo para transação é R$ " + MINIMUM_VALUE_TRANSACTION);
        }

        if (accountOrigin.getBalance().compareTo(amount) < 0) {
            throw new TransactionNotAuthorizedException("Saldo insuficiente.");
        }
    }

    public void validatePixTransfer(PixTransfer pixTransfer, Account accountOrigin) {

        String pixType = String.valueOf(pixTransfer.getPixType());
        String pixKey = pixTransfer.getPixKey();
        BigDecimal amount = pixTransfer.getAmount();

        if (accountOrigin.getStatus() == AccountStatus.DESATIVADA) {
            throw new UnauthorizedAccessException("A operação não pode ser concluída porque sua conta está desativada. Por favor, entre em contato com o suporte para mais informações ou vá até sua agência para assistência adicional.");
        }

        if (!isValidPix(pixType, pixKey)) {
            throw new ValidateException("Formato do documento inválido. CPF: XXX.XXX.XXX-XX, CNPJ: XX.XXX.XXX/XXXX-XX, PHONE: (XX)XXXXX-XXXX, EMAIL: name@email.com");
        }

        if (accountOrigin.getClient().getPixKey() != null &&
                accountOrigin.getClient().getPixKey().equals(pixTransfer.getPixKey())) {
            throw new UnauthorizedAccessException("Não é possível transferir dinheiro para a mesma conta");
        }

        if (accountOrigin.getBalance().compareTo(amount) < 0) {
            throw new TransactionNotAuthorizedException("Saldo insuficiente.");
        }

        if (amount == null) {
            throw new ValidateException("O valor da transferência não pode ser nulo.");
        }

        if (amount.compareTo(BigDecimal.ONE) < 0) {
            throw new TransactionNotAuthorizedException("O valor mínimo para transação é R$ 1,00.");
        }
    }

    private boolean isValidPix(String pixType, String pixKey) {

        if (pixType == null || pixType.trim().isEmpty() || pixKey == null || pixKey.trim().isEmpty()) {
            throw new ValidateException("Tipo de chave Pix e chave Pix não podem ser nulos ou vazios.");
        }

        return switch (pixType.toUpperCase()) {
            case "CPF" -> pixKey.matches("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}");
            case "CNPJ" -> pixKey.matches("\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}");
            case "PHONE" -> pixKey.matches("\\(\\d{2}\\)\\d{5}-\\d{4}");
            case "EMAIL" -> pixKey.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

            default -> throw new ValidateException("Tipo de chave Pix inválido: " + pixType);
        };
    }
}