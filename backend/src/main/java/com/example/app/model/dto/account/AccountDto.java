package com.example.app.model.dto.account;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AccountDto {
    private String id;
    private String accountNumber;
    private BigDecimal balance;
    private LocalDateTime createdAt;
}