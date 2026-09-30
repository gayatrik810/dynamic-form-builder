package com.example.dfbbackend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateDomainRequest {

    private String name;
    private String code;
    private String description;
}