package com.example.app.model.dto.financial;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class VerifyStatementDto {
    private Boolean isValid;
    private String message;
    private String selfName;
    private String selfAccountNumber;
    private BigDecimal amount;
    private String targetName;
    private String targetAccountNumber;
}
