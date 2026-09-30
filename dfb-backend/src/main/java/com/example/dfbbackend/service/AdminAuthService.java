package com.example.dfbbackend.service;

import com.example.dfbbackend.entity.Domain;
import com.example.dfbbackend.entity.Role;
import com.example.dfbbackend.entity.User;
import com.example.dfbbackend.entity.UserRole;
import com.example.dfbbackend.repository.DomainRepository;
import com.example.dfbbackend.repository.RoleRepository;
import com.example.dfbbackend.repository.UserRepository;
import com.example.dfbbackend.repository.UserRoleRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class AdminAuthService {


    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final UserRoleRepository userRoleRepository;

    private final DomainRepository domainRepository;

    private final PasswordEncoder passwordEncoder;


    // =============================================================
    // REGISTER ADMIN
    // =============================================================

    @Transactional
    public void registerAdmin(

            String name,

            String email,

            String password,

            Long domainId

    ) {


        // =========================================================
        // 1. CHECK IF EMAIL ALREADY EXISTS
        // =========================================================

        if (
                userRepository
                        .findByEmail(email)
                        .isPresent()
        ) {

            throw new RuntimeException(
                    "Email already registered"
            );

        }


        // =========================================================
        // 2. VALIDATE DOMAIN
        // =========================================================

        if (
                domainId == null
        ) {

            throw new RuntimeException(
                    "Domain selection is required"
            );

        }


        Domain domain =
                domainRepository
                        .findByIdAndActiveTrue(
                                domainId
                        )
                        .orElseThrow(
                                () ->
                                        new RuntimeException(
                                                "Invalid or inactive domain"
                                        )
                        );


        // =========================================================
        // 3. FIND ADMIN ROLE
        // =========================================================

        Role adminRole =
                roleRepository
                        .findByName(
                                "ADMIN"
                        )
                        .orElseThrow(
                                () ->
                                        new RuntimeException(
                                                "ADMIN role not found"
                                        )
                        );


        // =========================================================
        // 4. CREATE ADMIN USER
        // =========================================================

        LocalDateTime now =
                LocalDateTime.now();


        User admin =
                new User();


        admin.setName(
                name
        );


        admin.setEmail(
                email
        );


        admin.setPassword(
                passwordEncoder.encode(
                        password
                )
        );


        admin.setDomain(
                domain
        );


        admin.setActive(
                true
        );


        admin.setCreatedAt(
                now
        );


        admin.setUpdatedAt(
                now
        );


        admin =
                userRepository.save(
                        admin
                );


        // =========================================================
        // 5. ASSIGN ADMIN ROLE
        // =========================================================

        UserRole adminRoleEntity =
                new UserRole();


        adminRoleEntity.setUser(
                admin
        );


        adminRoleEntity.setRole(
                adminRole
        );


        userRoleRepository.save(
                adminRoleEntity
        );

    }

}