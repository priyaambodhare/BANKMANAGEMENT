package com.bank.management.bank_system.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bank.management.bank_system.entity.Deposit;
import com.bank.management.bank_system.service.DepositService;

import java.util.List;

@RestController
@RequestMapping("/api/deposits")
@CrossOrigin(origins = "http://localhost:4200") // Adjust for frontend
public class DepositController {

    @Autowired
    private DepositService depositService;

    // API to make a deposit
    @PostMapping
    public ResponseEntity<Deposit> depositAmount(@RequestBody Deposit deposit) {
        Deposit savedDeposit = depositService.makeDeposit(deposit);
        return ResponseEntity.ok(savedDeposit);
    }

    // API to get deposit history by account number
    @GetMapping("/{accountNumber}")
    public ResponseEntity<List<Deposit>> getDeposits(@PathVariable String accountNumber) {
        List<Deposit> deposits = depositService.getDepositsByAccountNumber(accountNumber);
        return ResponseEntity.ok(deposits);
    }
}

