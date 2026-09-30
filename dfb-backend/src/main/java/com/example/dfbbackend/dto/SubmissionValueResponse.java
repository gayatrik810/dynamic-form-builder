package com.example.dfbbackend.dto;

public record SubmissionValueResponse(

        Long fieldId,

        String fieldKey,

        String fieldLabel,

        String fieldType,

        Object value

) {
}