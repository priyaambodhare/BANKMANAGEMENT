package com.bank.management.bank_system.repository;

import com.bank.management.bank_system.entity.FundTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FundTransferRepository extends JpaRepository<FundTransfer, Long> {
}
