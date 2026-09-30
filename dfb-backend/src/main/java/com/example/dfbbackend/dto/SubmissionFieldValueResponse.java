package com.example.dfbbackend.dto;

public record SubmissionFieldValueResponse(

        Long fieldId,

        String fieldKey,

        String fieldLabel,

        String fieldType,

        Object value

) {
}