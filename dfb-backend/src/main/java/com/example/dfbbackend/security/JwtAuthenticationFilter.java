package com.example.dfbbackend.security;

import com.example.dfbbackend.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            UserService userService
    ) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        // No JWT supplied
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Remove "Bearer "
        String token = authHeader.substring(7);

        try {
            // Extract email from JWT
            String email = jwtService.extractEmail(token);

            // Only authenticate if nobody is already authenticated
            if (email != null &&
                    SecurityContextHolder.getContext().getAuthentication() == null) {

                // Load user from database
                UserDetails userDetails =
                        userService.loadUserByUsername(email);

                // Validate JWT
                if (jwtService.isTokenValid(token, email)) {

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    authentication.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request)
                    );

                    // Put authenticated user into Spring Security
                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(authentication);

                    System.out.println(
                            "JWT AUTHENTICATED USER: " + email
                    );
                }
            }

        } catch (Exception e) {

            // IMPORTANT: don't hide the actual JWT problem
            System.out.println(
                    "JWT ERROR: " +
                            e.getClass().getSimpleName()
            );

            System.out.println(
                    "JWT MESSAGE: " +
                            e.getMessage()
            );
        }

        // Continue request
        filterChain.doFilter(request, response);
    }
}