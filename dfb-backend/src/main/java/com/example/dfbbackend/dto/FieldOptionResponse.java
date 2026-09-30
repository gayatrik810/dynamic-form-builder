package com.example.dfbbackend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FieldOptionResponse {

    private Long id;
    private String label;
    private String value;
    private Integer displayOrder;
    private Boolean active;
}