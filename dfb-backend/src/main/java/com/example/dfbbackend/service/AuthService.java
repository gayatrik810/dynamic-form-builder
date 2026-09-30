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
public class AuthService {


    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final UserRoleRepository userRoleRepository;

    private final DomainRepository domainRepository;

    private final PasswordEncoder passwordEncoder;


    // =============================================================
    // REGISTER NORMAL USER
    //
    // IMPORTANT:
    // Users MUST select a domain.
    // Phone number is mandatory for normal users.
    // =============================================================

    @Transactional
    public void registerUser(

            String name,

            String email,

            String password,

            String phoneNumber,

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
        // 3. FIND USER ROLE
        // =========================================================

        Role userRole =
                roleRepository
                        .findByName(
                                "USER"
                        )
                        .orElseThrow(
                                () ->
                                        new RuntimeException(
                                                "USER role not found"
                                        )
                        );


        // =========================================================
        // 4. CREATE USER
        // =========================================================

        LocalDateTime now =
                LocalDateTime.now();


        User user =
                new User();


        user.setName(
                name
        );


        user.setEmail(
                email
        );


        // =========================================================
        // PHONE NUMBER
        // =========================================================

        user.setPhoneNumber(
                phoneNumber
        );


        user.setPassword(
                passwordEncoder.encode(
                        password
                )
        );


        user.setDomain(
                domain
        );


        user.setActive(
                true
        );


        user.setCreatedAt(
                now
        );


        user.setUpdatedAt(
                now
        );


        user =
                userRepository.save(
                        user
                );


        // =========================================================
        // 5. ASSIGN USER ROLE
        // =========================================================

        UserRole userRoleEntity =
                new UserRole();


        userRoleEntity.setUser(
                user
        );


        userRoleEntity.setRole(
                userRole
        );


        userRoleRepository.save(
                userRoleEntity
        );

    }


    // =============================================================
    // REGISTER ADMIN
    //
    // IMPORTANT:
    // Admin does NOT belong to any domain.
    //
    // domainId is intentionally ignored.
    // Admin domain_id will be NULL.
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
        // 2. NO DOMAIN VALIDATION FOR ADMIN
        //
        // Admin can manage ALL domains.
        //
        // Therefore domainId is intentionally ignored.
        // =========================================================


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


        // =========================================================
        // ADMIN HAS NO DOMAIN
        //
        // This stores NULL in users.domain_id.
        // =========================================================

        admin.setDomain(
                null
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