package com.example.app.service.financial;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.app.repository.StatementsRepository;
import com.example.app.repository.AccountsRepository;
import com.example.app.model.entity.accounts.Account;
import com.example.app.model.entity.financial.Statement;
import com.example.app.model.dto.financial.StatementDto;
import com.example.app.model.constant.StatementCode;
import com.example.app.model.constant.ChannelCode;

@Service
public class SaveDepositService {
    private static final Logger logger = LoggerFactory.getLogger(SaveDepositService.class);
    @Autowired
    private StatementsRepository statementsRepository;

    @Autowired
    private AccountsRepository accountsRepository;

    public StatementDto invoke(String accountNumber, BigDecimal amount, String terminalId) {
        validateInput(accountNumber, amount, terminalId);
        return saveDeposit(accountNumber, amount, terminalId);
    }

    private StatementDto saveDeposit(String accountNumber, BigDecimal amount, String terminalId) {
        Account account = accountsRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            logger.error("Invalid account number");
            throw new IllegalArgumentException("Invalid account number");
        }

        Statement lastStatement = statementsRepository.findTopByAccountIdOrderByCreatedAtDesc(account.getId());
        if(lastStatement == null) {
            lastStatement = new Statement();
            lastStatement.setBalance(BigDecimal.ZERO);
        }

        Statement statement = new Statement();
        statement.setAccount(account);
        statement.setCode(StatementCode.A0.name());
        statement.setChannel(ChannelCode.OTC.name());
        statement.setAmount(amount);
        statement.setBalance(lastStatement.getBalance().add(amount));
        statement.setRemarks("Deposit via Terminal " + terminalId);
        statement.setCreatedAt(LocalDateTime.now());

        logger.info("Saving deposit statement for account ID: {}, amount: {}, terminal ID: {}", account.getId(), amount, terminalId);
        statementsRepository.save(statement);
        
        return mapToStatementDto(statement);
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

    private void validateInput(String accountNumber, BigDecimal amount, String terminalId) {
        if (accountNumber == null || accountNumber.isEmpty()) {
            logger.error("Account Number cannot be null or empty");
            throw new IllegalArgumentException("Account Number cannot be null or empty");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            logger.error("Amount must be greater than zero");
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        if (terminalId == null || terminalId.isEmpty()) {
            logger.error("Terminal ID cannot be null or empty");
            throw new IllegalArgumentException("Terminal ID cannot be null or empty");
        }
    }
}