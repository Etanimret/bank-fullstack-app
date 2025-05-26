package com.example.app.service.accounts;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.app.repository.CustomersRepository;
import com.example.app.repository.StatementsRepository;
import com.example.app.repository.AccountsRepository;
import com.example.app.model.dto.account.CustomerDto;
import com.example.app.model.entity.accounts.Account;
import com.example.app.model.entity.accounts.Customer;
import com.example.app.model.entity.financial.Statement;
import com.example.app.model.dto.account.AccountDto;
import com.example.app.model.constant.TitleGenderCode;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RetrieveCustomerAccountService {
    private static final Logger logger = LoggerFactory.getLogger(RetrieveCustomerAccountService.class);

    @Autowired
    private CustomersRepository customersRepository;
    @Autowired
    private AccountsRepository accountsRepository;
    @Autowired
    private StatementsRepository statementsRepository;

    public CustomerDto invoke(String email, String password) {
        try {
            Customer customer = customersRepository.findByEmailAndPassword(email, password);
            if (customer == null) {
                logger.error("Email not found: {}", email);
                throw new RuntimeException("Email not found");
            }
            List<Account> accounts = accountsRepository.findAllByCustomer_Id(customer.getId());
            return mapToCustomerDto(customer, accounts);
        } catch (Exception e) {
            logger.error("Error occurred while retrieving account: {}", e.getMessage());
            throw new RuntimeException("Error occurred while retrieving account");
        }
    }

    private CustomerDto mapToCustomerDto(Customer customer, List<Account> accounts) {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setId(customer.getId().toString());
        customerDto.setEmail(customer.getEmail());
        customerDto.setPassword(maskPassword(customer.getPassword()));
        customerDto.setCitizenId(customer.getCitizenId());
        customerDto.setGender(TitleGenderCode.valueOf(customer.getGender()));
        customerDto.setAccountHolderNameTh(customer.getAccountHolderNameTh());
        customerDto.setAccountHolderNameEn(customer.getAccountHolderNameEn());
        customerDto.setPin(maskPassword(customer.getPin()));
        customerDto.setAccounts(accounts.stream()
                .map(account -> {
                    logger.info("Enriching account with ID: {}", account.getId());
                    AccountDto accountDto = new AccountDto();
                    accountDto.setId(account.getId().toString());
                    accountDto.setBalance(enrichAccountsWithBalance(account));
                    accountDto.setAccountNumber(account.getAccountNumber());
                    accountDto.setCreatedAt(account.getCreatedAt());
                    return accountDto;
                })
                .collect(Collectors.toList()));
        customerDto.setCreatedAt(customer.getCreatedAt());
        customerDto.setUpdatedAt(customer.getUpdatedAt());
        return customerDto;
    }

    private String maskPassword(String password) {
        return password.replaceAll(".", "*");
    }

    private BigDecimal enrichAccountsWithBalance(Account account) {
        Statement statement = statementsRepository.findTopByAccountIdOrderByCreatedAtDesc(account.getId());
        if (statement != null) {
            logger.info("balance for account ID is {}", statement.getBalance());
            return statement.getBalance();
        } else {
            logger.info("balance for account ID is 0");
            return BigDecimal.ZERO;
        }
    }
}
