package com.example.dfbbackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "form_submissions")
@Getter
@Setter
public class FormSubmission {

    // =========================================================
    // PRIMARY KEY
    // =========================================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // =========================================================
    // FORM
    // =========================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "form_id",
            nullable = false
    )
    private Form form;


    // =========================================================
    // VISIT TYPE
    // =========================================================
    // Every submission belongs to one specific Visit Type.
    //
    // Example:
    // Form       -> Hospital Visit Form
    // Visit Type -> Doctor Visit
    //
    // This is important for:
    // - Submission history
    // - User profile statistics
    // - Visit-type-wise submission counts
    // - Admin filtering
    // =========================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "visit_type_id",
            nullable = false
    )
    private VisitType visitType;


    // =========================================================
    // USER WHO SUBMITTED THE FORM
    // =========================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "submitted_by",
            nullable = false
    )
    private User submittedBy;


    // =========================================================
    // SUBMISSION TIMESTAMP
    // =========================================================

    @Column(
            name = "submitted_at",
            nullable = false
    )
    private LocalDateTime submittedAt;


    // =========================================================
    // LAST UPDATED TIMESTAMP
    // =========================================================

    @Column(
            name = "updated_at",
            nullable = false
    )
    private LocalDateTime updatedAt;


    // =========================================================
    // SUBMITTED FIELD VALUES
    // =========================================================
    //
    // One submission contains multiple SubmissionValue records.
    //
    // Example:
    //
    // Submission
    //      |
    //      |--- Name = Gayatri
    //      |
    //      |--- Phone = 9876543210
    //      |
    //      |--- Visit Date = 2026-09-05
    //
    // CascadeType.ALL:
    // Saving/deleting submission also handles its values.
    //
    // orphanRemoval:
    // Removing a value from the list removes it from DB.
    // =========================================================

    @OneToMany(
            mappedBy = "submission",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<SubmissionValue> values =
            new ArrayList<>();
}