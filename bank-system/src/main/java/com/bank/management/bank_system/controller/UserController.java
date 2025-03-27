package com.bank.management.bank_system.controller;

import com.bank.management.bank_system.entity.User;
import com.bank.management.bank_system.security.JwtUtil;
import com.bank.management.bank_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")

public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> userRequest) {
        String message = userService.registerUser(new User(
                null,
                userRequest.get("phoneNumber"),
                userRequest.get("fullName"),
                userRequest.get("email"),
                userRequest.get("address"),
                userRequest.get("password"),
                null
        ));
        return ResponseEntity.ok(Map.of("message", message));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        String phoneNumber = loginRequest.get("phoneNumber");
        String password = loginRequest.get("password");

        String token = userService.loginUser(phoneNumber, password);

        if (token != null) {
            return ResponseEntity.ok(Map.of("message", "Login successful", "token", token));
        } else {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }
    }

    @GetMapping("/validate-token")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String token) {
        String jwtToken = token.replace("Bearer ", "");
        String phoneNumber = jwtUtil.extractPhoneNumber(jwtToken);

        boolean isValid = jwtUtil.validateToken(jwtToken, phoneNumber);

        return ResponseEntity.ok(Map.of("status", isValid ? "Valid token" : "Invalid token"));
    }
    @GetMapping("/profile")
public ResponseEntity<?> getUserProfile(@RequestHeader("Authorization") String token) {
    String jwtToken = token.replace("Bearer ", "");
    String phoneNumber = jwtUtil.extractPhoneNumber(jwtToken);

    User user = userService.getUserByPhoneNumber(phoneNumber);
    if (user != null) {
        return ResponseEntity.ok(Map.of(
            "fullName", user.getFullName(),
            "phoneNumber", user.getPhoneNumber(),
            "email", user.getEmail(),
            "address", user.getAddress(),
            "accountNumber", user.getAccountNumber()
        ));
    } else {
        return ResponseEntity.status(404).body(Map.of("error", "User not found"));
    }
}
@PostMapping("/logout")
public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {
    String jwtToken = token.replace("Bearer ", "");
    
    if (jwtUtil.validateToken(jwtToken, jwtUtil.extractPhoneNumber(jwtToken))) {
        return ResponseEntity.ok(Map.of("message", "Logout successful"));
    } else {
        return ResponseEntity.status(401).body(Map.of("error", "Invalid token"));
    }
}


}
