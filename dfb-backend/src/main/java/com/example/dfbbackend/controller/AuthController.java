package com.example.dfbbackend.controller;

import com.example.dfbbackend.dto.LoginRequest;
import com.example.dfbbackend.dto.LoginResponse;
import com.example.dfbbackend.dto.RegisterRequest;
import com.example.dfbbackend.dto.UserResponse;
import com.example.dfbbackend.entity.User;
import com.example.dfbbackend.entity.UserRole;
import com.example.dfbbackend.repository.UserRepository;
import com.example.dfbbackend.repository.UserRoleRepository;
import com.example.dfbbackend.security.JwtService;
import com.example.dfbbackend.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AuthService authService;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    public AuthController(
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            AuthService authService,
            UserRepository userRepository,
            UserRoleRepository userRoleRepository
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.authService = authService;
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
    }

    // =============================================================
    // LOGIN
    // =============================================================

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        List<UserRole> userRoles =
                userRoleRepository.findByUserId(user.getId());

        String role = userRoles.isEmpty()
                ? null
                : userRoles.get(0).getRole().getName();

        String token = jwtService.generateToken(request.getEmail());

        UserResponse userResponse = new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhoneNumber(),
                role,
                user.getDomain() != null
                        ? user.getDomain().getId()
                        : null,
                user.getDomain() != null
                        ? user.getDomain().getName()
                        : null,
                user.getActive()
        );

        return new LoginResponse(
                token,
                userResponse
        );
    }

    // =============================================================
    // REGISTER
    // =============================================================

    @PostMapping("/register")
    public ResponseEntity<String> register(
            @Valid @RequestBody RegisterRequest request
    ) {

        authService.registerUser(
                request.getName(),
                request.getEmail(),
                request.getPassword(),
                request.getPhoneNumber(),
                request.getDomainId()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("User registered successfully");
    }
}