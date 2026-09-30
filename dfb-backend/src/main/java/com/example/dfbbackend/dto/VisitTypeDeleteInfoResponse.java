package com.example.dfbbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VisitTypeDeleteInfoResponse {

    private Long visitTypeId;

    private String visitTypeName;

    private long submissionCount;

    private boolean hasSubmissions;
}