package com.example.app.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import com.example.app.repository.AccountsRepositoryCustom;

@Repository
public class AccountsRepositoryImpl implements AccountsRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long getNextAccountNumberSeq() {
        return ((Number) entityManager.createNativeQuery("SELECT nextval('account_number_seq')").getSingleResult()).longValue();
    }
}
