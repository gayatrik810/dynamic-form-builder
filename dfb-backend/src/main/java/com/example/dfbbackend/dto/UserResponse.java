package com.example.dfbbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponse {

    private Long id;

    private String name;

    private String email;

    private String phoneNumber;

    private String role;

    private Long domainId;

    private String domainName;

    private Boolean active;
}