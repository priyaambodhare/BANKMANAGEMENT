package com.bank.management.bank_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.bank.management.bank_system.entity.Deposit;
import java.util.List;

@Repository
public interface DepositRepository extends JpaRepository<Deposit, Long> {

    // Fetch all deposits by account number
    List<Deposit> findByAccountNumber(String accountNumber);

    // Get the total balance (sum of all deposits)
    @Query("SELECT COALESCE(SUM(d.amount), 0) FROM Deposit d WHERE d.accountNumber = :accountNumber")
    Double getTotalBalanceByAccountNumber(String accountNumber);
}

