package com.example.app.model.entity.account;

import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.OneToOne;
import javax.persistence.CascadeType;
import java.time.LocalDateTime;
import com.example.app.model.entity.account.Account;

@Data
@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String email;

    @Column(length = 255, nullable = false)
    private String password;

    @Column(name = "citizen_id", length = 13, nullable = false)
    private String citizenId;

    @Column(length = 20, nullable = false)
    private String gender;

    @Column(name = "account_holder_name_th", length = 50, nullable = false)
    private String accountHolderNameTh;

    @Column(name = "account_holder_name_en", length = 50, nullable = false)
    private String accountHolderNameEn;

    @Column(length = 6, nullable = false)
    private String pin;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
    private Account account;
}