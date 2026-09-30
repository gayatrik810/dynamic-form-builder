package com.example.dfbbackend.dto;

import java.time.LocalDateTime;

public record SubmissionHistoryResponse(

        Long submissionId,

        Long formId,

        String formName,

        Long visitTypeId,

        String visitTypeName,

        Long submittedBy,

        LocalDateTime submittedAt

) {
}