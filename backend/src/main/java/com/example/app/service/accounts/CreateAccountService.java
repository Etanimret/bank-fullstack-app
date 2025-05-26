package com.example.app.service.accounts;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.app.repository.AccountsRepository;
import com.example.app.model.entity.accounts.Account;
import com.example.app.model.entity.accounts.Customer;

import java.time.LocalDateTime;
import com.example.app.repository.CustomersRepository;

@Service
public class CreateAccountService {
    @Autowired
    private AccountsRepository accountsRepository;

    @Autowired
    private CustomersRepository customersRepository;

    public String invoke(String citizenId) {
        String result = "";

        try {
            Customer customer = customersRepository.findByCitizenId(citizenId);
            if (customer == null) {
                throw new RuntimeException("Customer not found with citizenId: " + citizenId);
            }
            Account newAccount = initialAccount(customer);
            result = "Successfully created account. New account number : " + newAccount.getAccountNumber();
        } catch (Exception e) {
            result = "Error occurred while saving account";
        }
        return result;
    }

    private Account initialAccount(Customer customer) {
        Long seq = accountsRepository.getNextAccountNumberSeq();
        String accountNumber = String.format("%07d", seq);

        Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setCustomer(customer);
        account.setCreatedAt(LocalDateTime.now());

        return accountsRepository.save(account);
    }
}
