package com.example.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import com.example.app.service.financial.RetrieveAllStatementService;
import com.example.app.service.financial.SaveDepositService;
import com.example.app.service.financial.SaveWithdrawService;
import com.example.app.service.financial.VerifyTransferService;
import com.example.app.service.financial.SaveTransferService;
import com.example.app.model.dto.financial.StatementDto;
import com.example.app.model.dto.financial.VerifyStatementDto;

@RestController
public class StatmentsController {
    @Autowired
    private RetrieveAllStatementService retrieveAllStatementService;
    @Autowired
    private SaveDepositService saveDepositService;
    @Autowired
    private SaveWithdrawService saveWithdrawService;
    @Autowired
    private SaveTransferService saveTransferService;
    @Autowired
    private VerifyTransferService verifyTransferService;

    @GetMapping("/financial/retrieveAllStatements")
    public ResponseEntity<List<StatementDto>> getAllStatements(@RequestParam String accountNumber, @RequestParam LocalDateTime fromDate, @RequestParam LocalDateTime toDate) {
        return ResponseEntity.ok(retrieveAllStatementService.invoke(accountNumber, fromDate, toDate));
    }

    @PostMapping("/financial/deposit")
    public ResponseEntity<StatementDto> deposit(@RequestParam String accountNumber, @RequestParam BigDecimal amount, @RequestParam String terminalId) {
        return ResponseEntity.ok(saveDepositService.invoke(accountNumber, amount, terminalId));
    }
    
    @PostMapping("/financial/withdraw")
    public ResponseEntity<StatementDto> withdraw(@RequestParam String accountNumber, @RequestParam BigDecimal amount, @RequestParam String terminalId) {
        return ResponseEntity.ok(saveWithdrawService.invoke(accountNumber, amount, terminalId));
    }

    @PostMapping("/financial/transfer")
    public ResponseEntity<StatementDto> transfer(@RequestParam String selfAccountNumber, @RequestParam BigDecimal amount, @RequestParam String targetAccountNumber) {
        return ResponseEntity.ok(saveTransferService.invoke(selfAccountNumber, amount, targetAccountNumber));
    }

    @PostMapping("/financial/verify-transfer")
    public ResponseEntity<VerifyStatementDto> verifyTransfer(@RequestParam String selfAccountNumber, @RequestParam BigDecimal amount, @RequestParam String targetAccountNumber) {
        return ResponseEntity.ok(verifyTransferService.invoke(selfAccountNumber, amount, targetAccountNumber));
    }
}