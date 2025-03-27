package com.bank.management.bank_system.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "fund_transfer")
public class FundTransfer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String senderAccountNumber;
    private String receiverAccountNumber;
    private String receiverMobileNumber;
    private Double amount;
    private LocalDateTime transactionTime;
}
