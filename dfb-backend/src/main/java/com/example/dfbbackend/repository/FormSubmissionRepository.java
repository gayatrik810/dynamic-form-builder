package com.example.dfbbackend.repository;

import com.example.dfbbackend.entity.FormSubmission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FormSubmissionRepository
        extends JpaRepository<FormSubmission, Long> {

    // =========================================================
    // ADMIN - ALL SUBMISSIONS
    // =========================================================

    List<FormSubmission>
    findAllByOrderBySubmittedAtDesc();


    // =========================================================
    // USER - OWN SUBMISSION HISTORY
    // =========================================================

    List<FormSubmission>
    findBySubmittedBy_IdOrderBySubmittedAtDesc(
            Long userId
    );


    // =========================================================
    // USER - OWN SINGLE SUBMISSION
    // =========================================================

    Optional<FormSubmission>
    findByIdAndSubmittedBy_Id(
            Long submissionId,
            Long userId
    );


    // =========================================================
    // USER - TOTAL SUBMISSIONS
    // =========================================================

    long countBySubmittedBy_Id(
            Long userId
    );


    // =========================================================
    // USER - SUBMISSIONS FOR A SPECIFIC VISIT TYPE
    // =========================================================

    long countBySubmittedBy_IdAndVisitType_Id(
            Long userId,
            Long visitTypeId
    );


    // =========================================================
    // ALL SUBMISSIONS FOR A SPECIFIC VISIT TYPE
    // =========================================================
    //
    // Used by Admin before permanently deleting a Visit Type.
    //
    // If the count is greater than 0, the Admin will receive
    // a strong warning that related submission data will also
    // be permanently deleted.
    // =========================================================

    long countByVisitType_Id(
            Long visitTypeId
    );


    // =========================================================
    // ALL SUBMISSIONS FOR A SPECIFIC VISIT TYPE
    // =========================================================
    //
    // Used during permanent deletion.
    // =========================================================

    List<FormSubmission>
    findByVisitType_Id(
            Long visitTypeId
    );


    // =========================================================
    // USER - MOST RECENT SUBMISSION
    // =========================================================

    Optional<FormSubmission>
    findFirstBySubmittedBy_IdOrderBySubmittedAtDesc(
            Long userId
    );
}