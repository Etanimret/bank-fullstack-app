package com.example.app.service.accounts;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.app.repository.CustomersRepository;  

@Service
public class LoginAccountService {
    @Autowired
    private CustomersRepository customersRepository;

    public Boolean invoke(String email, String password) {
        try {
            Boolean existingCustomer = customersRepository.existsByEmailAndPassword(email, password);
            if (!existingCustomer) {
                throw new RuntimeException("Invalid email or password");
            }
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while retrieving account");
        }
    }
}
