package com.example.dfbbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class FormSubmissionResponse {

    private Long submissionId;

    private Long formId;

    private Long visitTypeId;

    private Long submittedBy;

    private LocalDateTime submittedAt;

    private String message;
}