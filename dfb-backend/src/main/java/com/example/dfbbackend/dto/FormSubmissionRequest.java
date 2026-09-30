package com.example.dfbbackend.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class FormSubmissionRequest {

    // =========================================================
    // FORM ID
    // =========================================================

    private Long formId;


    // =========================================================
    // VISIT TYPE ID
    // =========================================================
    // Specifies which Visit Type the user is submitting.
    // Every submission must belong to one Visit Type.
    // =========================================================

    private Long visitTypeId;


    // =========================================================
    // FORM FIELD VALUES
    // =========================================================
    //
    // Example:
    //
    // {
    //     "name": "Gayatri",
    //     "email": "test@example.com",
    //     "phone": "9876543210"
    // }
    //
    // The key is FormField.fieldKey.
    // =========================================================

    private Map<String, Object> values;
}