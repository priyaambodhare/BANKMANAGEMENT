package com.bank.management.bank_system.service;

import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class RechargeService {

    // ✅ Predefined recharge plans for different providers
    private static final Map<String, List<Map<String, Object>>> rechargePlans = Map.of(
        "Jio", List.of(
            Map.of("plan", "₹199", "validity", "28 Days", "data", "1.5GB/Day"),
            Map.of("plan", "₹399", "validity", "56 Days", "data", "2GB/Day")
        ),
        "Airtel", List.of(
            Map.of("plan", "₹179", "validity", "28 Days", "data", "1GB/Day"),
            Map.of("plan", "₹399", "validity", "56 Days", "data", "1.5GB/Day")
        ),
        "VI", List.of(
            Map.of("plan", "₹209", "validity", "28 Days", "data", "1GB/Day"),
            Map.of("plan", "₹479", "validity", "56 Days", "data", "2GB/Day")
        ),
        "BSNL", List.of(
            Map.of("plan", "₹147", "validity", "30 Days", "data", "2GB/Day"),
            Map.of("plan", "₹397", "validity", "60 Days", "data", "2GB/Day")
        )
    );

    // ✅ Get recharge plans based on provider
    public List<Map<String, Object>> getRechargePlans(String provider) {
        return rechargePlans.getOrDefault(provider, Collections.emptyList());
    }
}
