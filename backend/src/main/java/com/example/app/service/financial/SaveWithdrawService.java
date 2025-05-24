package com.example.app.service.financial;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.example.app.dto.StatementDto;
import com.example.app.entity.Account;
import com.example.app.entity.Statement;
import com.example.app.enums.StatementCode;
import com.example.app.enums.ChannelCode;
import com.example.app.repository.AccountsRepository;
import com.example.app.repository.StatementsRepository;

@Service
public class SaveWithdrawService {
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

        if (account.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient balance for withdrawal");
        }

        Statement lastStatement = statementsRepository.findTopByAccountIdOrderByCreatedAtDesc(accountId);

        Statement statement = new Statement();
        statement.setAccountId(account.getId());
        statement.setCode(StatementCode.A1.name());
        statement.setChannel(ChannelCode.OCT.name());
        statement.setAmount(amount.negate()); // Withdraw is a negative amount
        statement.setBalance(lastStatement.getBalance().subtract(amount));
        statement.setDescription("Withdrawal via Terminal " + terminalId);
        statement.setCreatedAt(LocalDateTime.now());

        statementsRepository.save(statement);

        return mapToStatementDto(statement);
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