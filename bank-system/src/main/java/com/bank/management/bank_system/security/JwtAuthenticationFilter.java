package com.bank.management.bank_system.security;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SecurityException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    // ✅ Constructor Injection (Best Practice)
    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        System.out.println("Request URI: " + requestURI);  // Log the request URI
    
        if (requestURI.equals("/api/admin/register") || requestURI.equals("/api/users/register")) {
            System.out.println("Skipping JWT authentication for: " + requestURI);  // Log when skipping authentication
            filterChain.doFilter(request, response);
            return;
        }
    
        // Continue with the filter logic...
    
    
    
        String authHeader = request.getHeader("Authorization");
    
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
    
        String token = authHeader.replace("Bearer ", "");
        String phoneNumber = null;
    
        try {
            phoneNumber = jwtUtil.extractPhoneNumber(token);
        } catch (SecurityException | MalformedJwtException e) {
            System.err.println("Invalid JWT signature: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            System.err.println("JWT token has expired: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("JWT Parsing Error: " + e.getMessage());
        }
    
        if (phoneNumber != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(phoneNumber);
    
            if (jwtUtil.validateToken(token, userDetails.getUsername())) {
                var authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
    
        filterChain.doFilter(request, response);
    }
    
}