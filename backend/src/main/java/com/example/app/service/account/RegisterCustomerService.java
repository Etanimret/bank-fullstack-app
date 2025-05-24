package com.example.app.service.account;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.app.repository.CustomersRepository;
import com.example.app.model.entity.account.Customer;
import com.example.app.model.dto.account.CustomerDto;
import java.time.LocalDateTime;

@Service
public class RegisterCustomerService {
    @Autowired
    private CustomersRepository customersRepository;

    public String invoke(CustomerDto customerDto) {
        
        String result = "";

        try {
            Boolean existingAccount = customersRepository.existsByCitizenId(customerDto.getCitizenId());
            if (existingAccount != null) {
                result = "Account already exists";
            } else {
                result = registerCustomer(customerDto);
                customersRepository.save(customerDto);
                result = "Register success";
            }
        } catch (Exception e) {
            result = "Error occurred while saving account";
        }
        return result;
    }

    private Customer registerCustomer(CustomerDto customerDto) {
        Customer customer = new Customer();
        customer.setEmail(customerDto.getEmail());
        customer.setPassword(customerDto.getPassword());
        customer.setCitizenId(customerDto.getCitizenId());
        customer.setGender(customerDto.getGender().name());
        customer.setAccountHolderNameTh(customerDto.getAccountHolderNameTh());
        customer.setAccountHolderNameEn(customerDto.getAccountHolderNameEn());
        customer.setPin(customerDto.getPin());
        customer.setCreatedAt(LocalDateTime.now());
        customer.setUpdatedAt(LocalDateTime.now());

        return customersRepository.save(customer);
    }
}
