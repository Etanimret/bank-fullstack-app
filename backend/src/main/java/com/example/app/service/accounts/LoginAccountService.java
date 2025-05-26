package com.example.app.service.accounts;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.app.repository.CustomersRepository;  

@Service
public class LoginAccountService {
    private static final Logger logger = LoggerFactory.getLogger(LoginAccountService.class);
    @Autowired
    private CustomersRepository customersRepository;

    public Boolean invoke(String email, String password) {
        try {
            Boolean existingCustomer = customersRepository.existsByEmailAndPassword(email, password);
            if (!existingCustomer) {
                logger.error("Invalid email or password for email: {}", email);
                throw new RuntimeException("Invalid email or password");
            }
            return true;
        } catch (Exception e) {
            logger.error("Error occurred while retrieving customers: {}", e.getMessage());
            throw new RuntimeException("Error occurred while retrieving customers");
        }
    }
}
