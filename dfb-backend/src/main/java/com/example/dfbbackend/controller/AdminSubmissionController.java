package com.example.dfbbackend.controller;

import com.example.dfbbackend.dto.AdminSubmissionResponse;
import com.example.dfbbackend.service.AdminSubmissionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/form-submissions")
public class AdminSubmissionController {

    private final AdminSubmissionService adminSubmissionService;

    public AdminSubmissionController(
            AdminSubmissionService adminSubmissionService
    ) {
        this.adminSubmissionService =
                adminSubmissionService;
    }

    // =====================================================
    // GET ALL SUBMISSIONS
    // =====================================================

    @GetMapping
    public List<AdminSubmissionResponse> getAllSubmissions() {

        return adminSubmissionService
                .getAllSubmissions();
    }

    // =====================================================
    // GET ONE SUBMISSION
    // =====================================================

    @GetMapping("/{submissionId}")
    public AdminSubmissionResponse getSubmissionById(
            @PathVariable Long submissionId
    ) {

        return adminSubmissionService
                .getSubmissionById(
                        submissionId
                );
    }
}