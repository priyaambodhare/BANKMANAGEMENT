package com.bank.management.bank_system.controller;

import com.bank.management.bank_system.service.FundTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class FundTransferController {

    @Autowired
    private FundTransferService fundTransferService;

    @PostMapping("/fund-transfer")
    public ResponseEntity<String> transferFunds(@RequestBody Map<String, Object> requestData) {
        String senderAccount = (String) requestData.get("senderAccount");
        String receiver = (String) requestData.get("receiver");
        Double amount = ((Number) requestData.get("amount")).doubleValue();
        boolean isMobileNumber = (Boolean) requestData.get("isMobileNumber");

        String result = fundTransferService.transferFunds(senderAccount, receiver, amount, isMobileNumber);
        return ResponseEntity.ok(result);
    }
}
