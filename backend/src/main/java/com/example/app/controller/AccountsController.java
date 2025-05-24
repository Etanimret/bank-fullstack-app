package com.example.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.app.service.SampleService;

@RestController
public class AccountsController {

    @Autowired
    private SaveAccountService saveAccountService;

    @GetMapping("/register-account")
    public String registerAccount() {
        return saveAccountService.registerAccount();
    }


    
    //Inquiry statement

    //Get account info

    //Transfer money
}