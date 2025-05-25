package com.example.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.app.model.dto.account.CustomerDto;
import com.example.app.service.accounts.CreateAccountService;
import com.example.app.service.accounts.LoginAccountService;
import com.example.app.service.accounts.RegisterCustomerService;
import com.example.app.service.accounts.RetrieveCustomerAccountService;

@RestController
public class AccountsController {
    @Autowired
    private RegisterCustomerService registerCustomerService;
    @Autowired
    private CreateAccountService createAccountService;
    @Autowired
    private RetrieveCustomerAccountService retrieveCustomerAccountService;
    @Autowired
    private LoginAccountService loginAccountService;

    @PostMapping("/customer/register-account")
    public ResponseEntity<String> registerAccount(@RequestBody CustomerDto customerDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(registerCustomerService.invoke(customerDto));
    }

    @PostMapping("/teller/create-account")
    public ResponseEntity<String> createAccount(@RequestParam String citizenId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(createAccountService.invoke(citizenId));
    }

    @GetMapping("/customer/retrieve-account")
    public ResponseEntity<CustomerDto> retrieveAccount(@RequestParam String email, @RequestParam String password) {
        return ResponseEntity.ok(retrieveCustomerAccountService.invoke(email, password));
    }

    @GetMapping("/customer/login")
    public ResponseEntity<Boolean> login(@RequestParam String email, @RequestParam String password) {
        try {
            return ResponseEntity.ok(loginAccountService.invoke(email, password));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
        }
    }
}