package com.example.app.model.dto.financial;

import lombok.Data;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import com.example.app.model.constant.StatementCode;
import com.example.app.model.constant.ChannelCode;

@Data
public class StatementDto {
    private Long id;
    private Long accountId;
    private StatementCode code;
    private ChannelCode channel;
    private BigDecimal amount;
    private BigDecimal balance;
    private String remarks;
    private LocalDateTime createdAt;
}
