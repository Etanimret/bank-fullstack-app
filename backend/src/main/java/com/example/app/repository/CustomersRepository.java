package com.example.app.repository;

import com.example.app.model.entity.account.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomersRepository extends JpaRepository<Customer, Long> {
    Boolean existsByCitizenId(String citizenId);
}