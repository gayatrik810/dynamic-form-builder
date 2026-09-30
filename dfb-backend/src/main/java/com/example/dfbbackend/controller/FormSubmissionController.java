package com.example.dfbbackend.controller;

import com.example.dfbbackend.dto.FormSubmissionRequest;
import com.example.dfbbackend.dto.FormSubmissionResponse;
import com.example.dfbbackend.dto.SubmissionDetailResponse;
import com.example.dfbbackend.dto.SubmissionHistoryResponse;
import com.example.dfbbackend.service.FormSubmissionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/form-submissions")
public class FormSubmissionController {

    private final FormSubmissionService formSubmissionService;


    public FormSubmissionController(
            FormSubmissionService formSubmissionService
    ) {
        this.formSubmissionService =
                formSubmissionService;
    }


    // =====================================================
    // SUBMIT FORM
    // =====================================================

    @PostMapping
    public ResponseEntity<FormSubmissionResponse> submitForm(
            @RequestBody FormSubmissionRequest request,
            Authentication authentication
    ) {

        FormSubmissionResponse response =
                formSubmissionService.submit(
                        request,
                        authentication
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    // =====================================================
    // GET LOGGED-IN USER SUBMISSION HISTORY
    // =====================================================

    @GetMapping("/my")
    public ResponseEntity<List<SubmissionHistoryResponse>>
    getMySubmissions(
            Authentication authentication
    ) {

        List<SubmissionHistoryResponse> response =
                formSubmissionService.getMySubmissions(
                        authentication
                );

        return ResponseEntity.ok(
                response
        );
    }


    // =====================================================
    // GET ONE SUBMITTED FORM
    // USER CAN VIEW BUT CANNOT MODIFY
    // =====================================================

    @GetMapping("/my/{submissionId}")
    public ResponseEntity<SubmissionDetailResponse>
    getMySubmissionById(
            @PathVariable Long submissionId,
            Authentication authentication
    ) {

        SubmissionDetailResponse response =
                formSubmissionService.getMySubmissionById(
                        submissionId,
                        authentication
                );

        return ResponseEntity.ok(
                response
        );
    }
}