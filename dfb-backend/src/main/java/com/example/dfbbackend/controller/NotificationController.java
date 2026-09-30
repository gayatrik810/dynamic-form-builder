package com.example.dfbbackend.controller;

import com.example.dfbbackend.dto.NotificationResponse;
import com.example.dfbbackend.entity.User;
import com.example.dfbbackend.repository.UserRepository;
import com.example.dfbbackend.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final UserRepository userRepository;

    // =========================================================
    // GET MY NOTIFICATIONS
    // =========================================================

    @GetMapping
    public List<NotificationResponse> getMyNotifications(
            Authentication authentication
    ) {

        User user =
                getCurrentUser(authentication);

        return notificationService
                .getMyNotifications(user);
    }

    // =========================================================
    // GET UNREAD COUNT
    // =========================================================

    @GetMapping("/unread-count")
    public long getUnreadCount(
            Authentication authentication
    ) {

        User user =
                getCurrentUser(authentication);

        return notificationService
                .getUnreadCount(user);
    }

    // =========================================================
    // MARK ONE AS READ
    // =========================================================

    @PatchMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(
            @PathVariable Long id,
            Authentication authentication
    ) {

        User user =
                getCurrentUser(authentication);

        notificationService.markAsRead(
                id,
                user
        );

        return ResponseEntity.noContent().build();
    }

    // =========================================================
    // MARK ALL AS READ
    // =========================================================

    @PatchMapping("/read-all")
    public ResponseEntity<Void> markAllAsRead(
            Authentication authentication
    ) {

        User user =
                getCurrentUser(authentication);

        notificationService.markAllAsRead(user);

        return ResponseEntity.noContent().build();
    }

    // =========================================================
    // GET CURRENT USER
    // =========================================================

    private User getCurrentUser(
            Authentication authentication
    ) {

        return userRepository
                .findByEmail(authentication.getName())
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"
                        )
                );
    }
}