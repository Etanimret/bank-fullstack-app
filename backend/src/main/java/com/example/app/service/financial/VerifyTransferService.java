package com.example.app.service.financial;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(VerifyTransferService.class);

    @Autowired
    private StatementsRepository statementsRepository;

    @Autowired
    private AccountsRepository accountsRepository;

    private String VALID_STATE = "Valid";

    public VerifyStatementDto invoke(String selfAccountNumber, BigDecimal amount, String targetAccountNumber) {
        return verifyTransfer(selfAccountNumber, amount, targetAccountNumber);
    }

    private VerifyStatementDto verifyTransfer(String selfAccountNumber, BigDecimal amount, String targetAccountNumber) {
        String message = "";
        message = validateInput(selfAccountNumber, amount, targetAccountNumber);

        VerifyStatementDto response = new VerifyStatementDto();

        if(message.isEmpty()) {
            try {
                Account selfAccount = accountsRepository.findByAccountNumber(selfAccountNumber);
                if (selfAccount == null) {
                    logger.error("Invalid account number: {}", selfAccountNumber);
                    throw new IllegalArgumentException("Invalid account number");
                }

                Account targetAccount = accountsRepository.findByAccountNumber(targetAccountNumber);
                if (targetAccount == null) {
                    logger.error("Invalid target account number: {}", targetAccountNumber);
                    throw new IllegalArgumentException("Invalid target account number");
                }

                Customer selfCustomer = selfAccount.getCustomer();
                Customer targetCustomer = targetAccount.getCustomer();

                Statement lastSelfStatement = statementsRepository.findTopByAccountIdOrderByCreatedAtDesc(selfAccount.getId());
                if (lastSelfStatement == null) {
                    lastSelfStatement = new Statement();
                    lastSelfStatement.setBalance(BigDecimal.ZERO);
                }

                if (lastSelfStatement.getBalance().compareTo(amount) < 0) {
                    logger.error("Insufficient balance for transfer");
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
                logger.error("Account ID not found");
                message = "Account ID not found";
            }
        } else {
            response.setIsValid(false);
            response.setMessage(message);
        }
        return response;
    }

    private String validateInput(String selfAccountNumber, BigDecimal amount, String targetAccountNumber) {
        if (selfAccountNumber == null || targetAccountNumber.isEmpty()) {
            logger.error("Account Number cannot be null or empty");
            return "Account Number cannot be null or empty";
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            logger.error("Amount must be greater than zero");
            return "Amount must be greater than zero";
        }
        if (targetAccountNumber == null || targetAccountNumber.isEmpty()) {
            logger.error("Target Account Number cannot be null or empty");
            return "Target Account Number cannot be null or empty";
        }
        if (selfAccountNumber.equals(targetAccountNumber)) {
            logger.error("Cannot transfer to the same account");
            return "Cannot transfer to the same account";
        }
        return "";
    }
}