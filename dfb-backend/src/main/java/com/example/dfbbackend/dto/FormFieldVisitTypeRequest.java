package com.example.dfbbackend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FormFieldVisitTypeRequest {

    @NotNull(message = "Form field id is required")
    private Long formFieldId;

    @NotNull(message = "Visit type id is required")
    private Long visitTypeId;
}