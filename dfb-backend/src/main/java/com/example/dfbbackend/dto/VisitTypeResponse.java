package com.example.dfbbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VisitTypeResponse {

    private Long id;
    private Long domainId;
    private String domainName;
    private String name;
    private Boolean active;
}