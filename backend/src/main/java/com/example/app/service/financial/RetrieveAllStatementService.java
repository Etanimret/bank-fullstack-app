package com.example.app.service.financial;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import com.example.app.repository.StatementsRepository;
import com.example.app.model.entity.financial.Statement;
import com.example.app.model.constant.ChannelCode;
import com.example.app.model.constant.StatementCode;
import com.example.app.model.dto.financial.StatementDto;

@Service
public class RetrieveAllStatementService {
    @Autowired
    private StatementsRepository statementsRepository;

    public List<StatementDto> invoke(String accountId) {
        UUID accountIdUuid = UUID.fromString(accountId);
        List<Statement> statements = statementsRepository.findAllByAccountId(accountIdUuid);
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
}
