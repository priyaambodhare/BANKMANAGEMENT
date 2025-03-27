package com.bank.management.bank_system.repository;

import com.bank.management.bank_system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByPhoneNumber(String phoneNumber);
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
    
    // ✅ Add method to find user by account number
    Optional<User> findByAccountNumber(String accountNumber);
}
