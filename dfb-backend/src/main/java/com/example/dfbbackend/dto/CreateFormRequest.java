package com.example.dfbbackend.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateFormRequest {

    private Long domainId;

    private String name;

    private String code;

    private String description;

    private List<Long> visitTypeIds;
}