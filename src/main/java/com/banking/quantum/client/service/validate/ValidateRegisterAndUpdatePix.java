package com.banking.quantum.client.service.validate;

import com.banking.quantum.client.domain.account.AccountStatus;
import com.banking.quantum.client.domain.client.Client;
import com.banking.quantum.client.domain.dto.transaction.PixKeyDto;
import com.banking.quantum.client.domain.repository.ClientRepository;
import com.banking.quantum.common.infra.exception.ValidateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ValidateRegisterAndUpdatePix {

    @Autowired
    private ClientRepository clientRepository;

    public void validateRegister(Client client, PixKeyDto pixKey) {

        validateCommon(client, pixKey);

        if (client.getPixKey() != null && !client.getPixKey().isEmpty()) {
            throw new ValidateException("Você já tem uma chave registrada. Se desejar, pode atualizá-la.");
        }
    }

    public void validateUpdate(Client client, PixKeyDto pixKey) {

        validateCommon(client, pixKey);
    }

    private void validateCommon(Client client, PixKeyDto pixKey) {

        Optional<Client> clientKeYPix = clientRepository.findByPixKey(pixKey.pixKey());

        if (clientKeYPix.isPresent()) {
            throw new ValidateException("A chave Pix que você tentou cadastrar já está em uso. Certifique-se de que a chave não pertence a outra conta ou banco. Tente uma chave diferente.");
        }

        if (!isValidKey(pixKey.pixKey())) {
            throw new ValidateException("Formato de chave Pix inválido. Por favor, verifique e tente novamente.");
        }

        if (client.getAccount().getStatus() == AccountStatus.DESATIVADA) {
            throw new ValidateException("A operação não pode ser concluída porque sua conta está desativada. Por favor, entre em contato com o suporte para mais informações ou vá até sua agência para assistência adicional.");
        }
    }

    private boolean isValidKey(String pixKey) {

        String email = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        String phone = "\\(\\d{2}\\) \\d{5}-\\d{4}";
        String cpf = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}";
        String cnpj = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

        return pixKey.matches(email) || pixKey.matches(phone) || pixKey.matches(cpf) || pixKey.matches(cnpj);
    }
}
