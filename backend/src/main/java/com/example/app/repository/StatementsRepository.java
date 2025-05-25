package com.example.app.repository;

import com.example.app.model.entity.financial.Statement;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface StatementsRepository extends JpaRepository<Statement, UUID> {
    List<Statement> findAllByAccountId(UUID accountId);
    Statement findTopByAccountIdOrderByCreatedAtDesc(UUID accountId);
}
