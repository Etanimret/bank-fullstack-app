package com.example.app.service.accounts;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.app.repository.CustomersRepository;
import com.example.app.repository.AccountsRepository;
import com.example.app.model.dto.account.CustomerDto;
import com.example.app.model.entity.accounts.Account;
import com.example.app.model.entity.accounts.Customer;
import com.example.app.model.dto.account.AccountDto;
import com.example.app.model.constant.TitleGenderCode;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RetrieveCustomerService {
    private static final Logger logger = LoggerFactory.getLogger(RetrieveCustomerService.class);

    @Autowired
    private CustomersRepository customersRepository;
    @Autowired
    private AccountsRepository accountsRepository;

    public CustomerDto invoke(String citizenId) {
        try {
            Customer customer = customersRepository.findByCitizenId(citizenId);
            if (customer == null) {
                logger.error("Citizen ID not found: {}", citizenId);
                throw new RuntimeException("Citizen ID not found");
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
        customerDto.setCitizenId(customer.getCitizenId());
        customerDto.setGender(TitleGenderCode.valueOf(customer.getGender()));
        customerDto.setAccountHolderNameTh(customer.getAccountHolderNameTh());
        customerDto.setAccountHolderNameEn(customer.getAccountHolderNameEn());
        customerDto.setAccounts(accounts.stream()
                .map(account -> {
                    AccountDto accountDto = new AccountDto();
                    accountDto.setId(account.getId().toString());
                    accountDto.setAccountNumber(account.getAccountNumber());
                    return accountDto;
                })
                .collect(Collectors.toList()));
        return customerDto;
    }
}
