package com.example.app.service.financial;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.app.model.dto.financial.StatementDto;
import com.example.app.model.entity.accounts.Account;
import com.example.app.model.entity.accounts.Customer;
import com.example.app.model.entity.financial.Statement;
import com.example.app.model.constant.StatementCode;
import com.example.app.model.constant.TitleGenderCode;
import com.example.app.model.constant.ChannelCode;
import com.example.app.repository.AccountsRepository;
import com.example.app.repository.StatementsRepository;

@Service
public class SaveTransferService {

    @Autowired
    private StatementsRepository statementsRepository;

    @Autowired
    private AccountsRepository accountsRepository;

    public StatementDto invoke(String selfAccountId, BigDecimal amount, String targetAccountId) {
        validateInput(selfAccountId, amount, targetAccountId);
        return saveTransfer(selfAccountId, amount, targetAccountId);
    }

    private StatementDto saveTransfer(String selfAccountId, BigDecimal amount, String targetAccountId) {
        UUID selfAccountUuid = UUID.fromString(selfAccountId);
        UUID targetAccountUuid = UUID.fromString(targetAccountId);

        Account selfAccount = accountsRepository.findById(selfAccountUuid)
            .orElseThrow(() -> new IllegalArgumentException("Invalid account ID"));
        Account targetAccount = accountsRepository.findById(targetAccountUuid)
            .orElseThrow(() -> new IllegalArgumentException("Invalid target account ID"));

        Customer selfCustomer = selfAccount.getCustomer();
        Customer targetCustomer = targetAccount.getCustomer();

        Statement lastSelfStatement = statementsRepository.findTopByAccountIdOrderByCreatedAtDesc(selfAccount.getId());
        Statement lastTargetStatement = statementsRepository.findTopByAccountIdOrderByCreatedAtDesc(targetAccount.getId());

        if (lastSelfStatement.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient balance for transfer");
        }

        Statement selfStatement = new Statement();
        selfStatement.setAccount(selfAccount);
        selfStatement.setCode(StatementCode.A2.name());
        selfStatement.setChannel(ChannelCode.ATS.name());
        selfStatement.setAmount(amount.negate());
        selfStatement.setBalance(lastSelfStatement.getBalance().subtract(amount));
        selfStatement.setRemarks(buildDescription("Transfer to", targetAccount, targetCustomer));
        selfStatement.setCreatedAt(LocalDateTime.now());

        Statement targetStatement = new Statement();
        targetStatement.setAccount(targetAccount);
        targetStatement.setCode(StatementCode.A3.name());
        targetStatement.setChannel(ChannelCode.ATS.name());
        targetStatement.setAmount(amount);
        targetStatement.setBalance(lastTargetStatement.getBalance().add(amount));
        targetStatement.setRemarks(buildDescription("Receive from", selfAccount, selfCustomer));
        targetStatement.setCreatedAt(LocalDateTime.now());

        statementsRepository.save(selfStatement);
        statementsRepository.save(targetStatement);

        return mapToStatementDto(selfStatement);
    }

    private StatementDto mapToStatementDto(Statement statement) {
        StatementDto dto = new StatementDto();
        dto.setId(statement.getId().toString());
        dto.setAccountId(statement.getAccount().getId().toString());
        dto.setCode(StatementCode.valueOf(statement.getCode()));
        dto.setChannel(ChannelCode.valueOf(statement.getChannel()));
        dto.setAmount(statement.getAmount());
        dto.setBalance(statement.getBalance());
        dto.setRemarks(statement.getRemarks());
        dto.setCreatedAt(statement.getCreatedAt());
        return dto;
    }

    private void validateInput(String selfAccountId, BigDecimal amount, String targetAccountId) {
        if (selfAccountId == null || selfAccountId.isEmpty()) {
            throw new IllegalArgumentException("Account ID cannot be null or empty");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        if (targetAccountId == null || targetAccountId.isEmpty()) {
            throw new IllegalArgumentException("Target Account ID cannot be null or empty");
        }
        if (selfAccountId.equals(targetAccountId)) {
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }
    }

    private String buildDescription(String startWord, Account account, Customer customer) {
        return String.format("%s %s %s %s",
            startWord,
            maskAccountNumber(account.getAccountNumber()),
            TitleGenderCode.valueOf(customer.getGender()).getTitles(),
            customer.getAccountHolderNameEn());
}

    private String maskAccountNumber(String accountNumber) {
        if (accountNumber == null || accountNumber.length() < 4) {
            return accountNumber;
        }
        return "x" + accountNumber.substring(accountNumber.length() - 4);
    }
}