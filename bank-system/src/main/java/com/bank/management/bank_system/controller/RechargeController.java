package com.bank.management.bank_system.controller;

import com.bank.management.bank_system.service.BalanceService;
import com.bank.management.bank_system.service.RechargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class RechargeController {

    @Autowired
    private RechargeService rechargeService;

    @Autowired
    private BalanceService balanceService;

    // ✅ Get available recharge plans
    @GetMapping("/recharge-plans/{provider}")
    public ResponseEntity<?> getRechargePlans(@PathVariable String provider) {
        List<Map<String, Object>> plans = rechargeService.getRechargePlans(provider);
        if (plans.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "No plans available for " + provider));
        }
        return ResponseEntity.ok(plans);
    }

    // ✅ Process recharge request
    @PostMapping("/recharge")
    public ResponseEntity<?> recharge(@RequestBody Map<String, String> request) {
        String accountNumber = request.get("accountNumber");
        String password = request.get("password");
        String mobileNumber = request.get("mobileNumber");
        String provider = request.get("provider");
        Double amount = Double.parseDouble(request.get("amount"));

        // ✅ Deduct balance if valid
        boolean isDeducted = balanceService.deductBalance(accountNumber, password, amount);
        if (!isDeducted) {
            return ResponseEntity.badRequest().body(Map.of("error", "Insufficient balance or invalid credentials!"));
        }

        return ResponseEntity.ok(Map.of("message", "Recharge successful for " + mobileNumber + " with ₹" + amount));
    }
}
