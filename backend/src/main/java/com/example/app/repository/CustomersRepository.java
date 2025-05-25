package com.example.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.app.model.entity.accounts.Customer;

import java.util.UUID;

public interface CustomersRepository extends JpaRepository<Customer, UUID> {
    Boolean existsByCitizenId(String citizenId);
    Customer findByCitizenId(String citizenId);
    Boolean existsByEmailAndPassword(String email, String password);
    Customer findByEmailAndPassword(String email, String password);
}
