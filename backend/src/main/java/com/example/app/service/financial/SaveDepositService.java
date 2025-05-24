package com.example.app.service.financial;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.example.app.repository.StatementsRepository;
import com.example.app.repository.AccountsRepository;
import com.example.app.model.entity.financial.Statement;
import com.example.app.model.entity.account.Account;
import com.example.app.model.dto.financial.StatementDto;
import com.example.app.model.constant.StatementCode;
import com.example.app.model.constant.ChannelCode;

@Service
public class SaveDepositService {
    @Autowired
    private StatementsRepository statementsRepository;

    @Autowired
    private AccountsRepository accountsRepository;

    public StatementDto invoke(String accountId, BigDecimal amount, String terminalId) {
        validateInput(accountId, amount, terminalId);
        return saveDeposit(accountId, amount, terminalId);
    }

    private StatementDto saveDeposit(String accountId, BigDecimal amount, String terminalId) {
        Account account = accountsRepository.findByAccountId(accountId);
    
        if (account == null) {
            throw new IllegalArgumentException("Invalid account ID");
        }

        Statement lastStatement = statementsRepository.findTopByAccountIdOrderByCreatedAtDesc(accountId);

        Statement statement = new Statement();
        statement.setAccountId(account.getId());
        statement.setCode(StatementCode.A0.name());
        statement.setChannel(ChannelCode.OCT.name());
        statement.setAmount(amount);
        statement.setBalance(lastStatement.getBalance().add(amount));
        statement.setDescription("Deposit via Terminal " + terminalId);
        statement.setCreatedAt(LocalDateTime.now());

        statementsRepository.save(statement);
        
        return mapToStatementDto(statement);
    }

    private StatementDto mapToStatementDto(Statement statement) {
        StatementDto dto = new StatementDto();
        dto.setId(statement.getId());
        dto.setAccountId(statement.getAccountId());
        dto.setCode(StatementCode.valueOf(statement.getCode()));
        dto.setChannel(ChannelCode.valueOf(statement.getChannel()));
        dto.setAmount(statement.getAmount());
        dto.setBalance(statement.getBalance());
        dto.setDescription(statement.getDescription());
        dto.setCreatedAt(statement.getCreatedAt());
        return dto;
    }

    private void validateInput(String accountId, BigDecimal amount, String terminalId) {
        if (accountId == null || accountId.isEmpty()) {
            throw new IllegalArgumentException("Account ID cannot be null or empty");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        if (terminalId == null || terminalId.isEmpty()) {
            throw new IllegalArgumentException("Terminal ID cannot be null or empty");
        }
    }
}