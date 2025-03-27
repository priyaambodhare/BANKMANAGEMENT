package com.bank.management.bank_system.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String phoneNumber; // Acts as username

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String password; // Will be hashed

    @Column(nullable = false, unique = true)
    private String accountNumber; // Generated manually

    @PrePersist
    public void generateAccountNumber() {
        if (this.accountNumber == null || this.accountNumber.isEmpty()) {
            this.accountNumber = "ACC" + UUID.randomUUID().toString().substring(0, 10);
        }
    }
}
