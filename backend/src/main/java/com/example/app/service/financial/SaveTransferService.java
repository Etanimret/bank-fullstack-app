package com.example.app.service.financial;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.app.dto.StatementDto;
import com.example.app.entity.Account;
import com.example.app.entity.Statement;
import com.example.app.enums.StatementCode;
import com.example.app.enums.ChannelCode;
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
        Account selfAccount = accountsRepository.findByAccountId(selfAccountId);
        Account targetAccount = accountsRepository.findByAccountId(targetAccountId);

        if (selfAccount == null) {
            throw new IllegalArgumentException("Invalid account ID");
        }

        if (selfAccount.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient balance for transfer");
        }

        if (targetAccount == null) {
            throw new IllegalArgumentException("Invalid target account ID");
        }
        
        Statement lastSelfStatement = statementsRepository.findTopByAccountIdOrderByCreatedAtDesc(selfAccountId);
        Statement lastTargetStatement = statementsRepository.findTopByAccountIdOrderByCreatedAtDesc(targetAccountId);

        Statement selfStatement = new Statement();
        selfStatement.setAccountId(selfAccount.getId());
        selfStatement.setCode(StatementCode.A2.name());
        selfStatement.setChannel(ChannelCode.ATS.name());
        selfStatement.setAmount(amount.negate());
        selfStatement.setBalance(lastSelfStatement.getBalance().subtract(amount));
        selfStatement.setDescription(buildDescription("Transfer to", targetAccount));
        selfStatement.setCreatedAt(LocalDateTime.now());

        Statement targetStatement = new Statement();
        targetStatement.setAccountId(targetAccount.getId());
        targetStatement.setCode(StatementCode.A3.name());
        targetStatement.setChannel(ChannelCode.ATS.name());
        targetStatement.setAmount(amount);
        targetStatement.setBalance(lastTargetStatement.getBalance().add(amount));
        targetStatement.setDescription(buildDescription("Receive from", selfAccount));
        targetStatement.setCreatedAt(LocalDateTime.now());

        statementsRepository.save(selfStatement);
        statementsRepository.save(targetStatement);

        return mapToStatementDto(selfStatement);
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
        if (selfAccount.getId().equals(targetAccount.getId())) {
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }
    }

    private String buildDescription(String startWord, Account account) {
    return String.format("%s %s %s %s",
            startWord,
            maskAccountNumber(account.getAccountNumber()),
            account.getGender().getTitles(),
            account.getAccountHolderNameEn());
}

    private String maskAccountNumber(String accountNumber) {
        if (accountNumber == null || accountNumber.length() < 4) {
            return accountNumber;
        }
        return "x" + accountNumber.substring(accountNumber.length() - 4);
    }
}