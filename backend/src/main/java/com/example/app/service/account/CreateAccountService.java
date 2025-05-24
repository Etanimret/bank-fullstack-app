package com.example.app.service.account;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.app.repository.AccountsRepository;
import com.example.app.model.entity.Account;
import com.example.app.model.dto.AccountDto;
import java.time.LocalDateTime;

@Service
public class CreateAccountService {
    @Autowired
    private AccountsRepository accountsRepository;

    public String invoke(AccountDto accountDto) {
        
        String result = "";

        try {
            result = initialAccount(accountDto);
            result = "Success";
            
        } catch (Exception e) {
            result = "Error occurred while saving account";
        }
        accountsRepository.save(accountDto);
        return result;
    }

    private Account initialAccount(AccountDto accountDto) {
        Long seq = accountsRepository.getNextAccountNumberSeq();
        String accountNumber = String.format("%07d", seq);

        Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setCreatedAt(LocalDateTime.now());

        return accountsRepository.save(account);
    }
}
