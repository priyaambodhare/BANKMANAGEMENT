package com.bank.management.bank_system.service;

import com.bank.management.bank_system.entity.FundTransfer;
import com.bank.management.bank_system.entity.Deposit;
import com.bank.management.bank_system.repository.DepositRepository;
import com.bank.management.bank_system.repository.UserRepository;
import com.bank.management.bank_system.repository.FundTransferRepository; // ✅ Missing import fixed

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class FundTransferService {

    @Autowired
    private FundTransferRepository fundTransferRepository; // ✅ Injected properly

    @Autowired
    private DepositRepository depositRepository;

    @Autowired
    private UserRepository userRepository;

    public String transferFunds(String senderAccount, String receiver, Double amount, boolean isMobileNumber) {
        // ✅ Use findByAccountNumber() instead of findById(Long)
        Optional<Deposit> senderDeposit = depositRepository.findByAccountNumber(senderAccount).stream().findFirst();

        if (!senderDeposit.isPresent() || senderDeposit.get().getAmount() < amount) {
            return "Insufficient balance.";
        }

        String receiverAccount;
        if (isMobileNumber) {
            receiverAccount = userRepository.findByPhoneNumber(receiver)
                    .map(user -> user.getAccountNumber())
                    .orElse(null);
        } else {
            receiverAccount = receiver;
        }

        if (receiverAccount == null) {
            return "Receiver account not found.";
        }

        // Deduct from sender
        Deposit sender = senderDeposit.get();
        sender.setAmount(sender.getAmount() - amount);
        depositRepository.save(sender);

        // Add to receiver
        Optional<Deposit> receiverDepositOpt = depositRepository.findByAccountNumber(receiverAccount).stream().findFirst();
        Deposit receiverDeposit = receiverDepositOpt.orElse(new Deposit(receiverAccount, 0.0));
        receiverDeposit.setAmount(receiverDeposit.getAmount() + amount);
        depositRepository.save(receiverDeposit);

        // Save transaction
        FundTransfer transfer = new FundTransfer();
        transfer.setSenderAccountNumber(senderAccount);
        transfer.setReceiverAccountNumber(receiverAccount);
        transfer.setAmount(amount);
        transfer.setTransactionTime(LocalDateTime.now());

        // ✅ Save transaction
        fundTransferRepository.save(transfer);

        return "Fund transfer successful!";
    }
}
