package com.example.dfbbackend.repository;

import com.example.dfbbackend.entity.UserRole;
import com.example.dfbbackend.entity.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRoleRepository
        extends JpaRepository<UserRole, UserRoleId> {

    List<UserRole> findByUserId(Long userId);
}