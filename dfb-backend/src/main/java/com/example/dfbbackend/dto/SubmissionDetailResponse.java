package com.example.dfbbackend.dto;

import java.time.LocalDateTime;
import java.util.List;

public record SubmissionDetailResponse(

        Long submissionId,

        Long formId,

        String formName,

        Long visitTypeId,

        String visitTypeName,

        Long submittedBy,

        LocalDateTime submittedAt,

        List<SubmissionFieldValueResponse> values

) {
}