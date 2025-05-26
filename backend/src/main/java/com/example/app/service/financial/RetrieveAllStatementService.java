package com.example.app.service.financial;

import org.springframework.stereotype.Service;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.example.app.repository.AccountsRepository;
import com.example.app.repository.StatementsRepository;
import com.example.app.model.entity.accounts.Account;
import com.example.app.model.entity.financial.Statement;
import com.example.app.model.constant.ChannelCode;
import com.example.app.model.constant.StatementCode;
import com.example.app.model.dto.financial.StatementDto;

@Service
public class RetrieveAllStatementService {
    private static final Logger logger = LoggerFactory.getLogger(RetrieveAllStatementService.class);
    @Autowired
    private StatementsRepository statementsRepository;
    @Autowired
    private AccountsRepository accountsRepository;

    public List<StatementDto> invoke(String accountNumber, LocalDateTime fromDate, LocalDateTime toDate) {
        validateInput(accountNumber, fromDate, toDate);
        LocalDateTime startOfMonth = fromDate.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endOfMonth = toDate.withDayOfMonth(toDate.toLocalDate().lengthOfMonth()).withHour(23).withMinute(59).withSecond(59).withNano(999999999);

        Account account = accountsRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            logger.error("Account not found for account number: {}", accountNumber);
            throw new IllegalArgumentException("Account not found");
        }

        UUID accountIdUuid = account.getId();

        logger.info("Retrieving statements for account ID: {} from {} to {}", accountIdUuid, startOfMonth, endOfMonth);
        List<Statement> statements =
            statementsRepository.findAllByAccountIdAndCreatedAtBetweenOrderByCreatedAtAsc(accountIdUuid, startOfMonth, endOfMonth);

        logger.info("Found {} statements for account ID: {}", statements.size(), accountIdUuid);
        return statements.stream()
                .map(statement -> mapToStatementDto(statement))
                .collect(Collectors.toList());
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

    private void validateInput(String accountNumber, LocalDateTime fromDate, LocalDateTime toDate) {
        if (accountNumber == null || accountNumber.isEmpty()) {
            logger.error("Account Number cannot be null or empty");
            throw new IllegalArgumentException("Account Number cannot be null or empty");
        }
        if (fromDate == null || toDate == null) {
            logger.error("From date and To date cannot be null");
            throw new IllegalArgumentException("From date and To date cannot be null");
        }
        if (fromDate.isAfter(toDate)) {
            logger.error("From date cannot be after To date");
            throw new IllegalArgumentException("From date cannot be after To date");
        }
    }
}
