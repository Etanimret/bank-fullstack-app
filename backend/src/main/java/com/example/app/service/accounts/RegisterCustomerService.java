package com.example.app.service.accounts;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.app.repository.CustomersRepository;
import com.example.app.model.dto.account.CustomerDto;
import com.example.app.model.entity.accounts.Customer;

import java.time.LocalDateTime;

@Service
public class RegisterCustomerService {
    private static final Logger logger = LoggerFactory.getLogger(RegisterCustomerService.class);

    @Autowired
    private CustomersRepository customersRepository;

    public String invoke(CustomerDto customerDto) {

        String result = "";
        try {
            Boolean existingAccount = customersRepository.existsByCitizenId(customerDto.getCitizenId());
            logger.info("Checking if account exists with citizenId result {}", existingAccount);
            if (existingAccount == true) {
                throw new IllegalArgumentException("Account already exists");
            } else {
                validateCustomerDto(customerDto);
                customersRepository.save(registerCustomer(customerDto));
                result = "Register success";
            }
        } catch (Exception e) {
            logger.error("Error occurred while saving account: {}", e.getMessage());
            throw new IllegalArgumentException("Error occurred while saving account");
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

    private void validateCustomerDto(CustomerDto customerDto) {
        validateEmail(customerDto.getEmail());
        validatePassword(customerDto.getPassword());
        validateCitizenDetails(customerDto.getCitizenId());
        validatePin(customerDto.getPin());

        if (customerDto.getCitizenId() == null || customerDto.getCitizenId().isEmpty()) {
            logger.error("Citizen ID is required");
            throw new IllegalArgumentException("Citizen ID is required");
        }
        if (customerDto.getAccountHolderNameTh() == null || customerDto.getAccountHolderNameTh().isEmpty()) {
            logger.error("Thai account holder name is required");
            throw new IllegalArgumentException("Thai account holder name is required");
        }
        if (customerDto.getAccountHolderNameEn() == null || customerDto.getAccountHolderNameEn().isEmpty()) {
            logger.error("English account holder name is required");
            throw new IllegalArgumentException("English account holder name is required");
        }
    }

    private void validateEmail(String email) {
        if (email == null || email.isEmpty()) {
            logger.error("Email is required");
            throw new IllegalArgumentException("Email is required");
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            logger.error("Invalid email format: {}", email);
            throw new IllegalArgumentException("Invalid email format");
        }
        if (email.length() > 255) {
            throw new IllegalArgumentException("Email is too long");
        }
    }

    private void validatePassword(String password) {
        if (password == null || password.isEmpty()) {
            logger.error("Password is required");
            throw new IllegalArgumentException("Password is required");
        }
        if (password.length() < 8 || password.length() > 20) {
            logger.error("Password must be between 8 and 20 characters");
            throw new IllegalArgumentException("Password must be between 8 and 20 characters");
        }
        if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$")) {
            logger.error("Password must contain at least one uppercase letter, one lowercase letter, and one digit");
            throw new IllegalArgumentException("Password must contain at least one uppercase letter, one lowercase letter, and one digit");
        }
    }

    private void validateCitizenDetails(String citizenId) {
        if (citizenId == null || citizenId.isEmpty()) {
            logger.error("Citizen ID is required");
            throw new IllegalArgumentException("Citizen ID is required");
        }
        if (!citizenId.matches("^\\d{13}$")) {
            logger.error("Citizen ID must be exactly 13 digits: {}", citizenId);
            throw new IllegalArgumentException("Citizen ID must be 13 digits");
        }
    }
    private void validatePin(String pin) {
        if (pin == null || pin.isEmpty()) {
            logger.error("PIN is required");
            throw new IllegalArgumentException("PIN is required");
        }
        if (pin.length() != 6) {
            logger.error("PIN must be exactly 6 digits: {}", pin);
            throw new IllegalArgumentException("PIN must be exactly 6 digits");
        }
        if (!pin.matches("^\\d{6}$")) {
            logger.error("PIN must contain only digits: {}", pin);
            throw new IllegalArgumentException("PIN must contain only digits");
        }
    }
}
