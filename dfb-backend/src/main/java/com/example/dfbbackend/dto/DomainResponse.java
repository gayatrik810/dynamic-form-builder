package com.example.dfbbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DomainResponse {

    private Long id;
    private String name;
    private String code;
    private String description;
    private Boolean active;
}