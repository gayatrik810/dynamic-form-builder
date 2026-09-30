package com.example.dfbbackend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FieldRuleResponse {

    private Long id;

    private Long sourceFieldId;

    private String operator;

    private String expectedValue;

    private Long targetFieldId;

    private String action;
}