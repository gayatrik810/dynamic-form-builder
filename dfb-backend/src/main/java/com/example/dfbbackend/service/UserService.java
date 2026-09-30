package com.example.dfbbackend.service;

import com.example.dfbbackend.dto.UserResponse;
import com.example.dfbbackend.entity.User;
import com.example.dfbbackend.entity.UserRole;
import com.example.dfbbackend.repository.UserRepository;
import com.example.dfbbackend.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final NotificationService notificationService;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));

        List<UserRole> userRoles =
                userRoleRepository.findByUserId(user.getId());

        var authorities = userRoles.stream()
                .map(userRole -> new SimpleGrantedAuthority(
                        "ROLE_" + userRole.getRole().getName()
                ))
                .toList();

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(authorities)
                .disabled(!user.getActive())
                .build();
    }

    public List<UserResponse> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(this::buildUserResponse)
                .filter(user ->
                        "USER".equalsIgnoreCase(user.getRole())
                )
                .toList();
    }

    public UserResponse deactivateUser(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        /*
         * Create the notification BEFORE deactivating the user.
         *
         * NotificationService intentionally ignores inactive users.
         * Therefore, the notification must be created while the user
         * is still active.
         */
        notificationService.notifyUser(
                user,
                null,
                "USER_DEACTIVATED",
                "Account Deactivated",
                "Your account has been deactivated by an administrator.",
                "USER",
                user.getId()
        );

        // Deactivate the user after creating the notification
        user.setActive(false);

        userRepository.save(user);

        return buildUserResponse(user);
    }

    public UserResponse activateUser(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        // Activate the user first
        user.setActive(true);

        userRepository.save(user);

        /*
         * The user is now active, so NotificationService
         * will allow the activation notification to be created.
         */
        notificationService.notifyUser(
                user,
                null,
                "USER_ACTIVATED",
                "Account Activated",
                "Your account has been activated by an administrator.",
                "USER",
                user.getId()
        );

        return buildUserResponse(user);
    }

    private UserResponse buildUserResponse(User user) {

        List<UserRole> userRoles =
                userRoleRepository.findByUserId(user.getId());

        String role = userRoles.stream()
                .map(userRole ->
                        userRole.getRole().getName()
                )
                .findFirst()
                .orElse("USER");

        Long domainId = null;
        String domainName = null;

        if (user.getDomain() != null) {
            domainId = user.getDomain().getId();
            domainName = user.getDomain().getName();
        }

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhoneNumber(),
                role,
                domainId,
                domainName,
                user.getActive()
        );
    }
}