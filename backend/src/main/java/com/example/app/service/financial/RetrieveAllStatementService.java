package com.example.app.service.financial;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.stream.Collectors;
import com.example.app.repository.StatementsRepository;
import com.example.app.model.entity.financial.Statement;
import com.example.app.model.dto.financial.StatementDto;

@Service
public class RetrieveAllStatementService {
    @Autowired
    private StatementsRepository statementsRepository;

    public List<StatementDto> invoke(String accountId) {
        List<Statement> statements = statementsRepository.findAllByAccountId(accountId);
        return statements.stream()
                .map(statement -> mapToStatementDto(statement))
                .collect(Collectors.toList());
    }

    private StatementDto mapToStatementDto(Statement statement) {
        StatementDto dto = new StatementDto();
        dto.setId(statement.getId());
        dto.setAccountId(statement.getAccountId());
        dto.setCode(statement.getCode());
        dto.setChannel(statement.getChannel());
        dto.setAmount(statement.getAmount());
        dto.setBalance(statement.getBalance());
        dto.setDescription(statement.getDescription());
        dto.setCreatedAt(statement.getCreatedAt());
        return dto;
    }
}
