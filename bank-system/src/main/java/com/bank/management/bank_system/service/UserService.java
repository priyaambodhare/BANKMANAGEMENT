package com.bank.management.bank_system.service;

import com.bank.management.bank_system.entity.User;
import com.bank.management.bank_system.repository.UserRepository;
import com.bank.management.bank_system.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;  // ✅ Inject JwtUtil

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private static final Random RANDOM = new Random();

    private String generateAccountNumber() {
        return "AC" + (100000 + RANDOM.nextInt(900000)); // Example: AC123456
    }

    public String registerUser(User user) {
        if (userRepository.existsByPhoneNumber(user.getPhoneNumber()) ||
            userRepository.existsByEmail(user.getEmail())) {
            return "Phone number or email already exists!";
        }

        // Encrypt password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Generate and set a unique account number BEFORE saving
        user.setAccountNumber(generateAccountNumber());

        // Save user to DB with account number
        userRepository.save(user);

        return "User registered successfully with Account No: " + user.getAccountNumber();
    }

    public String loginUser(String phoneNumber, String password) {
        Optional<User> userOptional = userRepository.findByPhoneNumber(phoneNumber);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                return jwtUtil.generateToken(phoneNumber); // ✅ Return JWT token on successful login
            }
        }
        return null; // Invalid login credentials
    }

    // ✅ Add method to extract phone number from JWT token
    public String extractPhoneNumber(String token) {
        return jwtUtil.extractPhoneNumber(token);
    }

    // ✅ Add method to validate JWT token
    public boolean validateToken(String token, String phoneNumber) {
        String extractedPhoneNumber = jwtUtil.extractPhoneNumber(token); // ✅ Fix here
        return extractedPhoneNumber.equals(phoneNumber);
    }
    public User getUserByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber).orElse(null);
    }
    
    
}
