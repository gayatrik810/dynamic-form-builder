package com.example.dfbbackend.dto;

import java.time.LocalDateTime;
import java.util.Map;

public class FormSubmissionAdminResponse {

    private Long submissionId;
    private Long formId;
    private String submittedBy;
    private LocalDateTime submittedAt;
    private Map<String, Object> values;

    public FormSubmissionAdminResponse(
            Long submissionId,
            Long formId,
            String submittedBy,
            LocalDateTime submittedAt,
            Map<String, Object> values
    ) {
        this.submissionId = submissionId;
        this.formId = formId;
        this.submittedBy = submittedBy;
        this.submittedAt = submittedAt;
        this.values = values;
    }

    public Long getSubmissionId() {
        return submissionId;
    }

    public Long getFormId() {
        return formId;
    }

    public String getSubmittedBy() {
        return submittedBy;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public Map<String, Object> getValues() {
        return values;
    }
}