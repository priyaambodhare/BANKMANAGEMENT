package com.bank.management.bank_system.entity;



import lombok.Data;

@Data
public class FundTransferRequest {
    private String recipient; // Can be Account Number or Mobile Number
    private double amount;
}

