package com.bank.management.bank_system.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expirationTime;

    private SecretKey getSigningKey() {
        return new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256"); // ✅ New method for JWT 0.12+
    }

    public String generateToken(String phoneNumber) {
        return Jwts.builder()
                .subject(phoneNumber)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSigningKey()) // ✅ Updated for JWT 0.12+
                .compact();
    }

    public boolean validateToken(String token, String phoneNumber) {
        try {
            var claims = Jwts.parser()
                    .verifyWith(getSigningKey()) // ✅ Correct for 0.12+
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return claims.getSubject().equals(phoneNumber);
        } catch (Exception e) {
            System.out.println("JWT Validation Failed: " + e.getMessage());
            return false;
        }
    }

    public String extractPhoneNumber(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey()) // ✅ Updated for JWT 0.12+
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(extractAllClaims(token));
    }
}
