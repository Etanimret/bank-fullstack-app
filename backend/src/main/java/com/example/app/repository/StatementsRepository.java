package com.example.app.repository;

import com.example.app.model.entity.financial.Statement;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StatementsRepository extends JpaRepository<Statement, Long> {
    List<Statement> findAllByAccountId(Long accountId);
    Statement findTopByAccountIdOrderByCreatedAtDesc(Long accountId);
}
