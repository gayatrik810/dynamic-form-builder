package com.example.dfbbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class FormResponse {

    private Long id;

    private Long domainId;

    private String domainName;

    private String name;

    private String code;

    private String description;

    private Boolean active;

    private List<Long> visitTypeIds;

    private List<String> visitTypeNames;
}