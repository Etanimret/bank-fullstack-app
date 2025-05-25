package com.example.app.model.dto.account;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

import com.example.app.model.constant.TitleGenderCode;

@Data
public class CustomerDto {
    private String id;
    private String email;
    private String password;
    private String citizenId;
    private TitleGenderCode gender;
    private String accountHolderNameTh;
    private String accountHolderNameEn;
    private String pin;
    private List<AccountDto> accounts;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}