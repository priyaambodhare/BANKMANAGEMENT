package com.bank.management.bank_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank.management.bank_system.entity.Deposit;
import com.bank.management.bank_system.entity.User;
import com.bank.management.bank_system.repository.DepositRepository;
import com.bank.management.bank_system.repository.UserRepository;

import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BalanceService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DepositRepository depositRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // ✅ Validate user credentials and get balance
    public Double validateAndGetBalance(String accountNumber, String password) {
        Optional<User> userOptional = userRepository.findByAccountNumber(accountNumber);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(password, user.getPassword())) { // ✅ Check hashed password
                return depositRepository.getTotalBalanceByAccountNumber(accountNumber);
            }
        }
        return null; // Invalid credentials
    }

    // ✅ Deduct balance for recharge
    @Transactional
    public boolean deductBalance(String accountNumber, String password, Double amount) {
        Optional<User> userOptional = userRepository.findByAccountNumber(accountNumber);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            
            if (passwordEncoder.matches(password, user.getPassword())) { // ✅ Password verification
                Double currentBalance = depositRepository.getTotalBalanceByAccountNumber(accountNumber);
                
                if (currentBalance != null && currentBalance >= amount) {
                    // Deduct balance by inserting a negative transaction
                    Deposit deduction = new Deposit();
                    deduction.setAccountNumber(accountNumber);
                    deduction.setAmount(-amount);  // Negative deposit entry to reduce balance
                    depositRepository.save(deduction);

                    return true; // Deduction successful
                }
            }
        }
        return false; // Invalid credentials or insufficient balance
    }
}
