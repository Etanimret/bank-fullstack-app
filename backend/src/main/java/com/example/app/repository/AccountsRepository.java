package com.example.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.app.model.entity.accounts.Account;

import java.util.List;
import java.util.UUID;

public interface AccountsRepository extends JpaRepository<Account, UUID>, AccountsRepositoryCustom {
    Account findByAccountNumber(String accountNumber);
    List<Account> findAllByCustomer_Id(UUID customerId);
}
