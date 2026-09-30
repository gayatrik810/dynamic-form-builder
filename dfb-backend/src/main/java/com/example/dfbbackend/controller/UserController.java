package com.example.dfbbackend.controller;

import com.example.dfbbackend.dto.UserResponse;
import com.example.dfbbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // =============================================================
    // GET ALL USERS
    // =============================================================

    @GetMapping
    public List<UserResponse> getAllUsers() {

        return userService.getAllUsers();
    }

    // =============================================================
    // DEACTIVATE USER
    // =============================================================

    @PatchMapping("/{id}/deactivate")
    public UserResponse deactivateUser(
            @PathVariable Long id
    ) {

        return userService.deactivateUser(id);
    }

    // =============================================================
    // ACTIVATE USER
    // =============================================================

    @PatchMapping("/{id}/activate")
    public UserResponse activateUser(
            @PathVariable Long id
    ) {

        return userService.activateUser(id);
    }
}