package com.example.dfbbackend.service;

import com.example.dfbbackend.dto.AdminSubmissionResponse;
import com.example.dfbbackend.dto.SubmissionFieldValueResponse;
import com.example.dfbbackend.entity.FormSubmission;
import com.example.dfbbackend.entity.SubmissionValue;
import com.example.dfbbackend.exception.ResourceNotFoundException;
import com.example.dfbbackend.repository.FormSubmissionRepository;
import com.example.dfbbackend.repository.SubmissionValueRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class AdminSubmissionService {

    private final FormSubmissionRepository formSubmissionRepository;
    private final SubmissionValueRepository submissionValueRepository;

    public AdminSubmissionService(
            FormSubmissionRepository formSubmissionRepository,
            SubmissionValueRepository submissionValueRepository
    ) {
        this.formSubmissionRepository =
                formSubmissionRepository;

        this.submissionValueRepository =
                submissionValueRepository;
    }

    // =========================================================
    // GET ALL SUBMISSIONS
    // =========================================================

    public List<AdminSubmissionResponse> getAllSubmissions() {

        List<FormSubmission> submissions =
                formSubmissionRepository
                        .findAllByOrderBySubmittedAtDesc();

        return submissions
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // =========================================================
    // GET SUBMISSION BY ID
    // =========================================================

    public AdminSubmissionResponse getSubmissionById(
            Long submissionId
    ) {

        FormSubmission submission =
                formSubmissionRepository
                        .findById(submissionId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Submission not found with id: "
                                                + submissionId
                                )
                        );

        return toResponse(submission);
    }

    // =========================================================
    // CONVERT ENTITY TO RESPONSE
    // =========================================================

    private AdminSubmissionResponse toResponse(
            FormSubmission submission
    ) {

        /*
         * Get all values belonging to this submission.
         *
         * The repository method orders them according to
         * the display order of the form fields.
         */
        List<SubmissionValue> submissionValues =
                submissionValueRepository
                        .findBySubmission_IdOrderByField_DisplayOrderAsc(
                                submission.getId()
                        );

        /*
         * Convert SubmissionValue entities into the same
         * response structure used by the normal submission
         * detail API.
         */
        List<SubmissionFieldValueResponse> values =
                submissionValues
                        .stream()
                        .map(this::toFieldValueResponse)
                        .toList();

        /*
         * Form
         */
        Long formId =
                submission.getForm() != null
                        ? submission.getForm().getId()
                        : null;

        String formName =
                submission.getForm() != null
                        ? submission.getForm().getName()
                        : null;

        /*
         * Visit Type
         */
        Long visitTypeId =
                submission.getVisitType() != null
                        ? submission.getVisitType().getId()
                        : null;

        String visitTypeName =
                submission.getVisitType() != null
                        ? submission.getVisitType().getName()
                        : null;

        /*
         * Domain
         *
         * The form belongs to a domain in the current
         * DFB structure.
         */
        Long domainId = null;
        String domainName = null;

        if (submission.getForm() != null
                && submission.getForm().getDomain() != null) {

            domainId =
                    submission.getForm()
                            .getDomain()
                            .getId();

            domainName =
                    submission.getForm()
                            .getDomain()
                            .getName();
        }

        /*
         * Submitted By
         */
        Long submittedById =
                submission.getSubmittedBy() != null
                        ? submission.getSubmittedBy().getId()
                        : null;

        String submittedByName =
                submission.getSubmittedBy() != null
                        ? submission.getSubmittedBy().getName()
                        : null;

        String submittedByEmail =
                submission.getSubmittedBy() != null
                        ? submission.getSubmittedBy().getEmail()
                        : null;

        /*
         * Build complete admin response.
         */
        return new AdminSubmissionResponse(

                submission.getId(),

                formId,
                formName,

                visitTypeId,
                visitTypeName,

                domainId,
                domainName,

                submittedById,
                submittedByName,
                submittedByEmail,

                submission.getSubmittedAt(),

                values
        );
    }

    // =========================================================
    // CONVERT SUBMISSION VALUE TO RESPONSE
    // =========================================================

    private SubmissionFieldValueResponse toFieldValueResponse(
            SubmissionValue submissionValue
    ) {

        if (submissionValue.getField() == null) {

            return new SubmissionFieldValueResponse(
                    null,
                    null,
                    null,
                    null,
                    null
            );
        }

        return new SubmissionFieldValueResponse(

                submissionValue
                        .getField()
                        .getId(),

                submissionValue
                        .getField()
                        .getFieldKey(),

                submissionValue
                        .getField()
                        .getLabel(),

                submissionValue
                        .getField()
                        .getFieldType(),

                getActualValue(submissionValue)
        );
    }

    // =========================================================
    // GET ACTUAL SUBMISSION VALUE
    // =========================================================

    private Object getActualValue(
            SubmissionValue submissionValue
    ) {

        if (submissionValue.getValueString() != null) {

            return submissionValue.getValueString();
        }

        if (submissionValue.getValueNumber() != null) {

            return submissionValue.getValueNumber();
        }

        if (submissionValue.getValueBoolean() != null) {

            return submissionValue.getValueBoolean();
        }

        if (submissionValue.getValueDate() != null) {

            return submissionValue.getValueDate();
        }

        if (submissionValue.getValueDatetime() != null) {

            return submissionValue.getValueDatetime();
        }

        return null;
    }
}