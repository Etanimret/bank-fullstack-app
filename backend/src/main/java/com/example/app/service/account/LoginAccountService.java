package com.example.app.service.account;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.app.repository.CustomersRepository;
import com.example.app.model.entity.customer.Customer;

@Service
public class LoginAccountService {
    @Autowired
    private CustomersRepository customersRepository;

    public Boolean invoke(String email, String password) {
        try {
            Customer customer = customersRepository.findByEmail(email);
            if (customer == null) {
                throw new RuntimeException("Customer account not found");
            }
            if (!customer.getPassword().equals(password)) {
                throw new RuntimeException("Invalid email or password");
            }
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while retrieving account");
        }
    }
}
