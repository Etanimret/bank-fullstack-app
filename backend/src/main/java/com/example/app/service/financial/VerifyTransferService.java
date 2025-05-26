package com.example.app.service.financial;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.app.model.dto.financial.VerifyStatementDto;
import com.example.app.model.entity.accounts.Account;
import com.example.app.model.entity.accounts.Customer;
import com.example.app.model.entity.financial.Statement;
import com.example.app.repository.AccountsRepository;
import com.example.app.repository.StatementsRepository;

@Service
public class VerifyTransferService {

    @Autowired
    private StatementsRepository statementsRepository;

    @Autowired
    private AccountsRepository accountsRepository;

    private String VALID_STATE = "Valid";

    public VerifyStatementDto invoke(String selfAccountId, BigDecimal amount, String targetAccountId) {
        return verifyTransfer(selfAccountId, amount, targetAccountId);
    }

    private VerifyStatementDto verifyTransfer(String selfAccountId, BigDecimal amount, String targetAccountId) {
        String message = "";
        message = validateInput(selfAccountId, amount, targetAccountId);

        VerifyStatementDto response = new VerifyStatementDto();

        if(message.isEmpty()) {
            UUID selfAccountUuid = UUID.fromString(selfAccountId);
            UUID targetAccountUuid = UUID.fromString(targetAccountId);

            try {
                Account selfAccount = accountsRepository.findById(selfAccountUuid)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid account ID"));
                Account targetAccount = accountsRepository.findById(targetAccountUuid)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid target account ID"));

                Customer selfCustomer = selfAccount.getCustomer();
                Customer targetCustomer = targetAccount.getCustomer();

                Statement lastSelfStatement = statementsRepository.findTopByAccountIdOrderByCreatedAtDesc(selfAccount.getId());

                if (lastSelfStatement.getBalance().compareTo(amount) < 0) {
                    message = "Insufficient balance for transfer";
                } else {
                    message = VALID_STATE;
                }

                response.setIsValid(message.equals(VALID_STATE));
                response.setMessage(message);
                response.setSelfName(selfCustomer.getAccountHolderNameEn());
                response.setSelfAccountNumber(selfAccount.getAccountNumber());
                response.setAmount(amount);
                response.setTargetName(targetCustomer.getAccountHolderNameEn());
                response.setTargetAccountNumber(targetAccount.getAccountNumber());
            } catch (Exception e) {
                message = "Account ID not found";
            }
        } else {
            response.setIsValid(false);
            response.setMessage(message);
        }
        return response;
    }

    private String validateInput(String selfAccountId, BigDecimal amount, String targetAccountId) {
        if (selfAccountId == null || selfAccountId.isEmpty()) {
            return "Account ID cannot be null or empty";
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return "Amount must be greater than zero";
        }
        if (targetAccountId == null || targetAccountId.isEmpty()) {
            return "Target Account ID cannot be null or empty";
        }
        if (selfAccountId.equals(targetAccountId)) {
            return "Cannot transfer to the same account";
        }
        return "";
    }
}