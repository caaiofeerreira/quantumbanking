package com.banking.quantum.client.domain.account;

import java.math.BigDecimal;

public interface AccountOperation {

    void credit(BigDecimal amount);

    void debit(BigDecimal amount);
}