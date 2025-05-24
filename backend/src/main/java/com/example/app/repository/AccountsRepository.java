package com.example.app.repository;

import com.example.app.model.entity.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.app.repository.AccountsRepositoryCustom;

public interface AccountsRepository extends JpaRepository<Account, Long>, AccountsRepositoryCustom {
    Account findByAccountNumber(String accountNumber);
    List<Account> findAllByCustomer_Id(Long customerId);
}
