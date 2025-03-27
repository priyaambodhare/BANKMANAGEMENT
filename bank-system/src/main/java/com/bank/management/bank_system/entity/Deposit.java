package com.bank.management.bank_system.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "deposits")
public class Deposit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountNumber; // Fetched from logged-in user
    private Double amount; // Deposit amount
    private LocalDateTime depositDate; // Timestamp when deposit is made

    @PrePersist
    public void setDepositDate() {
        this.depositDate = LocalDateTime.now();
    }

    // ✅ Add this constructor
    public Deposit(String accountNumber, Double amount) {
        this.accountNumber = accountNumber;
        this.amount = amount;
        this.depositDate = LocalDateTime.now();
    }
}
