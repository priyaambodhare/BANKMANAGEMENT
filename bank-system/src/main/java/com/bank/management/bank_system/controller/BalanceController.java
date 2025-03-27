package com.bank.management.bank_system.controller;

import com.bank.management.bank_system.service.BalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class BalanceController {

    @Autowired
    private BalanceService balanceService;

    @PostMapping("/check-balance")
    public ResponseEntity<?> checkBalance(@RequestBody Map<String, String> request) {
        String accountNumber = request.get("accountNumber");
        String password = request.get("password");

        if (accountNumber == null || password == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Account number or password is missing"));
        }

        Double totalBalance = balanceService.validateAndGetBalance(accountNumber, password);

        if (totalBalance == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid credentials or balance not found"));
        }

        return ResponseEntity.ok(Map.of("balance", totalBalance));
    }
}
