package com.example.app.model.dto.account;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CustomerDto {
    private Long id;
    private String email;
    private String password;
    private String citizenId;
    private TitleGenderCode gender;
    private String accountHolderNameTh;
    private String accountHolderNameEn;
    private String pin;
    private AccountDto account;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}