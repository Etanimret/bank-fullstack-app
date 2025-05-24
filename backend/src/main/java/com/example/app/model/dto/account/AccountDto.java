package com.example.app.model.dto.account;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AccountDto {
    private Long id;
    private String accountNumber;
    private LocalDateTime createdAt;
}