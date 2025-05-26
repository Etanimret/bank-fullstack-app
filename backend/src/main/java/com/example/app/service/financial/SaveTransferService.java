package com.example.app.service.financial;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(SaveTransferService.class);
    @Autowired
    private StatementsRepository statementsRepository;

    @Autowired
    private AccountsRepository accountsRepository;

    public StatementDto invoke(String selfAccountNumber, BigDecimal amount, String targetAccountNumber) {
        validateInput(selfAccountNumber, amount, targetAccountNumber);
        return saveTransfer(selfAccountNumber, amount, targetAccountNumber);
    }

    private StatementDto saveTransfer(String selfAccountNumber, BigDecimal amount, String targetAccountNumber) {
        Account selfAccount = accountsRepository.findByAccountNumber(selfAccountNumber);
        if (selfAccount == null) {
            logger.error("Invalid account number: {}", selfAccountNumber);
            throw new IllegalArgumentException("Invalid account number");
        }

        Account targetAccount = accountsRepository.findByAccountNumber(targetAccountNumber);
        if (targetAccount == null) {
            logger.error("Invalid target account number: {}", targetAccountNumber);
            throw new IllegalArgumentException("Invalid target account number");
        }

        Customer selfCustomer = selfAccount.getCustomer();
        Customer targetCustomer = targetAccount.getCustomer();

        Statement lastSelfStatement = statementsRepository.findTopByAccountIdOrderByCreatedAtDesc(selfAccount.getId());
        if(lastSelfStatement == null) {
            lastSelfStatement = new Statement();
            lastSelfStatement.setBalance(BigDecimal.ZERO);
        }
        Statement lastTargetStatement = statementsRepository.findTopByAccountIdOrderByCreatedAtDesc(targetAccount.getId());
        if(lastTargetStatement == null) {
            lastTargetStatement = new Statement();
            lastTargetStatement.setBalance(BigDecimal.ZERO);
        }

        if (lastSelfStatement.getBalance().compareTo(amount) < 0) {
            logger.error("Insufficient balance for transfer. Account ID: {}, Balance: {}, Transfer Amount: {}", 
                selfAccount.getId(), lastSelfStatement.getBalance(), amount);
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

        logger.info("Start save self statement");
        statementsRepository.save(selfStatement);
        logger.info("Start save target statement");
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

    private void validateInput(String selfAccountNumber, BigDecimal amount, String targetAccountNumber) {
        if (selfAccountNumber == null || selfAccountNumber.isEmpty()) {
            logger.error("Account Number cannot be null or empty");
            throw new IllegalArgumentException("Account Number cannot be null or empty");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            logger.error("Amount must be greater than zero");
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        if (targetAccountNumber == null || targetAccountNumber.isEmpty()) {
            logger.error("Target Account Number cannot be null or empty");
            throw new IllegalArgumentException("Target Account Number cannot be null or empty");
        }
        if (selfAccountNumber.equals(targetAccountNumber)) {
            logger.error("Cannot transfer to the same account");
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