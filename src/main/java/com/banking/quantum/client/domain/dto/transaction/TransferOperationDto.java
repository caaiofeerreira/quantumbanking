package com.banking.quantum.client.domain.dto.transaction;

import java.math.BigDecimal;

public record TransferOperationDto(String name,
                                   String accountDestiny,
                                   BigDecimal amount,
                                   String accountType,
                                   String agencyNumber,
                                   String bankCode,
                                   String document,
                                   String cpf,
                                   String phone,
                                   String email,
                                   String pixKey,
                                   String pixType) {
}