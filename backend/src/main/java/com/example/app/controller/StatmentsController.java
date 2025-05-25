package com.example.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;
import com.example.app.service.financial.RetrieveAllStatementService;
import com.example.app.service.financial.SaveDepositService;
import com.example.app.service.financial.SaveWithdrawService;
import com.example.app.service.financial.SaveTransferService;
import com.example.app.model.dto.financial.StatementDto;

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

    @GetMapping("/financial/retrieveAllStatements")
    public ResponseEntity<List<StatementDto>> getAllStatements(@RequestParam String accountId) {
        return ResponseEntity.ok(retrieveAllStatementService.invoke(accountId));
    }

    @PostMapping("/financial/deposit")
    public ResponseEntity<StatementDto> deposit(@RequestParam String accountId, @RequestParam BigDecimal amount, @RequestParam String terminalId) {
        return ResponseEntity.ok(saveDepositService.invoke(accountId, amount, terminalId));
    }
    
    @PostMapping("/financial/withdraw")
    public ResponseEntity<StatementDto> withdraw(@RequestParam String accountId, @RequestParam BigDecimal amount, @RequestParam String terminalId) {
        return ResponseEntity.ok(saveWithdrawService.invoke(accountId, amount, terminalId));
    }

    @PostMapping("/financial/transfer")
    public ResponseEntity<StatementDto> transfer(@RequestParam String selfAccountId, @RequestParam BigDecimal amount, @RequestParam String targetAccountId) {
        return ResponseEntity.ok(saveTransferService.invoke(selfAccountId, amount, targetAccountId));
    }
}