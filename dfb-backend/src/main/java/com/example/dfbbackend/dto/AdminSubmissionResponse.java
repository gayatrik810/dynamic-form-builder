package com.example.dfbbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class AdminSubmissionResponse {

    private Long submissionId;

    private Long formId;
    private String formName;

    private Long visitTypeId;
    private String visitTypeName;

    private Long domainId;
    private String domainName;

    private Long submittedById;
    private String submittedByName;
    private String submittedByEmail;

    private LocalDateTime submittedAt;

    private List<SubmissionFieldValueResponse> values;
}