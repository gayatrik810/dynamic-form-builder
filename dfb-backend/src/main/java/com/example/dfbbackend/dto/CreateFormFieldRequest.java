package com.example.dfbbackend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateFormFieldRequest {

    private String fieldKey;

    private String label;

    private String fieldType;

    private Boolean required;

    private Integer displayOrder;

    private String placeholder;

    private String validationRule;
}