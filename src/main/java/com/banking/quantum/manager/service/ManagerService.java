package com.banking.quantum.manager.service;

import com.banking.quantum.client.domain.dto.account.AccountClosureDto;
import com.banking.quantum.client.domain.dto.account.AccountDto;
import com.banking.quantum.manager.domain.dto.ApprovalRequest;

import java.util.List;

public interface ManagerService {

    List<AccountDto> getAccounts(String token);

    List<AccountClosureDto> accountClosureRequest(String token);

    void finalizeAccountClosure(String token, ApprovalRequest approve);

    List<AccountDto> closedAccounts(String token);
}
