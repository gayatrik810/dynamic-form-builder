package com.example.dfbbackend.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class DynamicFieldResponse {

    private Long id;

    private String fieldKey;

    private String label;

    private String fieldType;

    private Boolean required;

    private Integer displayOrder;

    private String placeholder;

    private String validationRule;

    private Boolean active;

    private List<FieldOptionResponse> options = new ArrayList<>();

    private List<FieldRuleResponse> rules = new ArrayList<>();
}