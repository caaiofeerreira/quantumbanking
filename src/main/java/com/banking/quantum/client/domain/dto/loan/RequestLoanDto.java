package com.banking.quantum.client.domain.dto.loan;

import java.math.BigDecimal;

public record RequestLoanDto(BigDecimal amount,
                             int installment) {
}