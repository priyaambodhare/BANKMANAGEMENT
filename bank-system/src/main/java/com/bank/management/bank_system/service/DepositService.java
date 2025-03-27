package com.bank.management.bank_system.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank.management.bank_system.entity.Deposit;
import com.bank.management.bank_system.repository.DepositRepository;

import java.util.List;

@Service
public class DepositService {

    @Autowired
    private DepositRepository depositRepository;

    public Deposit makeDeposit(Deposit deposit) {
        return depositRepository.save(deposit);
    }

    public List<Deposit> getDepositsByAccountNumber(String accountNumber) {
        return depositRepository.findByAccountNumber(accountNumber);
    }
}

