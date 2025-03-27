package com.bank.management.bank_system.service;

import com.bank.management.bank_system.entity.User;
import com.bank.management.bank_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        User user = userRepository.findByPhoneNumber(phoneNumber)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with phone number: " + phoneNumber));
        
        return new CustomUserDetails(user); // Return instance of CustomUserDetails
    }
}
