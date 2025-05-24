package com.example.app.service.account;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.app.repository.CustomersRepository;
import com.example.app.repository.AccountsRepository;
import com.example.app.model.entity.account.Customer;
import com.example.app.model.entity.account.Account;
import com.example.app.model.dto.account.CustomerDto;

@Service
public class RetrieveCustomerAccountService {
    @Autowired
    private CustomersRepository customersRepository;
    @Autowired
    private AccountsRepository accountsRepository;

    public CustomerDto invoke(String email) {
        try {
            Customer customer = customersRepository.findByEmail(email);
            if (customer == null) {
                throw new RuntimeException("Customer not found");
            }
            Account account = accountsRepository.findByCustomerId(customer.getId());
            return mapToCustomerDto(customer, account);
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while retrieving account");
        }
    }

    private CustomerDto mapToCustomerDto(Customer customer, Account account) {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setEmail(customer.getEmail());
        customerDto.setCitizenId(customer.getCitizenId());
        customerDto.setGender(TitleGenderCode.valueOf(customer.getGender()));
        customerDto.setAccountHolderNameTh(customer.getAccountHolderNameTh());
        customerDto.setAccountHolderNameEn(customer.getAccountHolderNameEn());
        customerDto.setPin(customer.getPin());
        customerDto.setAccountNumber(account.getAccountNumber());
        return customerDto;
    }
}
