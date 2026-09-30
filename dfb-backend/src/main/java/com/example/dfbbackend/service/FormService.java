package com.example.dfbbackend.service;

import com.example.dfbbackend.dto.CreateFormRequest;
import com.example.dfbbackend.entity.Domain;
import com.example.dfbbackend.entity.Form;
import com.example.dfbbackend.entity.FormVisitType;
import com.example.dfbbackend.entity.FormVisitTypeId;
import com.example.dfbbackend.entity.User;
import com.example.dfbbackend.entity.VisitType;
import com.example.dfbbackend.exception.ResourceNotFoundException;
import com.example.dfbbackend.repository.DomainRepository;
import com.example.dfbbackend.repository.FormRepository;
import com.example.dfbbackend.repository.FormVisitTypeRepository;
import com.example.dfbbackend.repository.UserRepository;
import com.example.dfbbackend.repository.VisitTypeRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class FormService {

    private final FormRepository formRepository;
    private final DomainRepository domainRepository;
    private final VisitTypeRepository visitTypeRepository;
    private final FormVisitTypeRepository formVisitTypeRepository;
    private final JdbcTemplate jdbcTemplate;

    private final NotificationService notificationService;
    private final UserRepository userRepository;

    public FormService(
            FormRepository formRepository,
            DomainRepository domainRepository,
            VisitTypeRepository visitTypeRepository,
            FormVisitTypeRepository formVisitTypeRepository,
            JdbcTemplate jdbcTemplate,
            NotificationService notificationService,
            UserRepository userRepository
    ) {
        this.formRepository = formRepository;
        this.domainRepository = domainRepository;
        this.visitTypeRepository = visitTypeRepository;
        this.formVisitTypeRepository = formVisitTypeRepository;
        this.jdbcTemplate = jdbcTemplate;
        this.notificationService = notificationService;
        this.userRepository = userRepository;
    }

    // =========================================================
    // CREATE FORM
    // =========================================================

    public Form createForm(CreateFormRequest request) {

        validateRequest(request);

        if (formRepository.findByCode(request.getCode()).isPresent()) {
            throw new IllegalArgumentException(
                    "Form code already exists: " + request.getCode()
            );
        }

        Domain domain = domainRepository.findById(request.getDomainId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Domain not found with id: "
                                        + request.getDomainId()
                        )
                );

        if (!Boolean.TRUE.equals(domain.getActive())) {
            throw new IllegalArgumentException(
                    "Cannot create form for an inactive domain"
            );
        }

        Form form = new Form();

        form.setDomain(domain);
        form.setName(request.getName().trim());
        form.setCode(request.getCode().trim());

        form.setDescription(
                request.getDescription() == null
                        ? null
                        : request.getDescription().trim()
        );

        form.setActive(true);

        Form savedForm = formRepository.save(form);

        // Create Form ↔ Visit Type mappings
        saveVisitTypeMappings(
                savedForm,
                request.getVisitTypeIds(),
                domain
        );

        // =====================================================
        // NOTIFY USERS OF THIS DOMAIN
        // =====================================================

        User admin = getAuthenticatedUser();

        notificationService.notifyDomainUsers(
                domain.getId(),
                admin,
                "FORM_CREATED",
                "New Form Added",
                "A new form '" + savedForm.getName()
                        + "' was added to your domain.",
                "FORM",
                savedForm.getId()
        );

        return savedForm;
    }

    // =========================================================
    // GET ALL FORMS
    // =========================================================

    @Transactional(readOnly = true)
    public List<Form> getAllForms() {

        return formRepository.findAll();
    }

    // =========================================================
    // GET FORM BY ID
    // =========================================================

    @Transactional(readOnly = true)
    public Form getFormById(Long id) {

        return formRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Form not found with id: " + id
                        )
                );
    }

    // =========================================================
    // UPDATE FORM
    // =========================================================

    public Form updateForm(
            Long id,
            CreateFormRequest request
    ) {

        validateRequest(request);

        Form form = getFormById(id);

        // Keep the old domain before updating
        Long oldDomainId =
                form.getDomain() != null
                        ? form.getDomain().getId()
                        : null;

        if (!form.getCode().equals(request.getCode().trim())) {

            if (formRepository.findByCode(request.getCode().trim())
                    .filter(existingForm ->
                            !existingForm.getId().equals(id)
                    )
                    .isPresent()) {

                throw new IllegalArgumentException(
                        "Form code already exists: "
                                + request.getCode()
                );
            }
        }

        Domain domain = domainRepository.findById(request.getDomainId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Domain not found with id: "
                                        + request.getDomainId()
                        )
                );

        if (!Boolean.TRUE.equals(domain.getActive())) {
            throw new IllegalArgumentException(
                    "Cannot assign form to an inactive domain"
            );
        }

        form.setDomain(domain);

        form.setName(
                request.getName().trim()
        );

        form.setCode(
                request.getCode().trim()
        );

        form.setDescription(
                request.getDescription() == null
                        ? null
                        : request.getDescription().trim()
        );

        Form savedForm =
                formRepository.save(form);

        // Replace existing Visit Type mappings
        formVisitTypeRepository.deleteByForm_Id(id);

        saveVisitTypeMappings(
                savedForm,
                request.getVisitTypeIds(),
                domain
        );

        // =====================================================
        // NOTIFY AFFECTED DOMAIN USERS
        // =====================================================

        User admin = getAuthenticatedUser();

        /*
         * If the form stayed in the same domain,
         * users receive one notification.
         *
         * If the form moved to another domain,
         * users from both affected domains are notified.
         */

        notificationService.notifyDomainUsers(
                domain.getId(),
                admin,
                "FORM_UPDATED",
                "Form Updated",
                "The form '" + savedForm.getName()
                        + "' was updated by an administrator.",
                "FORM",
                savedForm.getId()
        );

        if (oldDomainId != null &&
                !oldDomainId.equals(domain.getId())) {

            notificationService.notifyDomainUsers(
                    oldDomainId,
                    admin,
                    "FORM_UPDATED",
                    "Form Updated",
                    "A form in your domain was updated or moved by an administrator.",
                    "FORM",
                    savedForm.getId()
            );
        }

        return savedForm;
    }

    // =========================================================
    // DEACTIVATE FORM
    // =========================================================

    public Form deactivateForm(Long id) {

        Form form = getFormById(id);

        if (!Boolean.TRUE.equals(form.getActive())) {
            return form;
        }

        form.setActive(false);

        Form savedForm = formRepository.save(form);

        // =====================================================
        // NOTIFY DOMAIN USERS
        // =====================================================

        if (savedForm.getDomain() != null) {

            User admin = getAuthenticatedUser();

            notificationService.notifyDomainUsers(
                    savedForm.getDomain().getId(),
                    admin,
                    "FORM_DEACTIVATED",
                    "Form Deactivated",
                    "The form '" + savedForm.getName()
                            + "' has been deactivated and is no longer available.",
                    "FORM",
                    savedForm.getId()
            );
        }

        return savedForm;
    }

    // =========================================================
    // ACTIVATE FORM
    // =========================================================

    public Form activateForm(Long id) {

        Form form = getFormById(id);

        if (Boolean.TRUE.equals(form.getActive())) {
            return form;
        }

        if (form.getDomain() == null ||
                !Boolean.TRUE.equals(form.getDomain().getActive())) {

            throw new IllegalArgumentException(
                    "Cannot activate form because its domain is inactive"
            );
        }

        form.setActive(true);

        Form savedForm = formRepository.save(form);

        // =====================================================
        // NOTIFY DOMAIN USERS
        // =====================================================

        User admin = getAuthenticatedUser();

        notificationService.notifyDomainUsers(
                savedForm.getDomain().getId(),
                admin,
                "FORM_ACTIVATED",
                "Form Activated",
                "The form '" + savedForm.getName()
                        + "' is now active and available.",
                "FORM",
                savedForm.getId()
        );

        return savedForm;
    }

    // =========================================================
    // DELETE FORM
    // =========================================================
    //
    // Deletes dependent records first so that MySQL foreign-key
    // constraints are respected.
    //
    // Order:
    //
    // submission_values
    //       ↓
    // form_submissions
    //       ↓
    // field_rules
    //       ↓
    // form_field_visit_types
    //       ↓
    // field_options
    //       ↓
    // form_fields
    //       ↓
    // form_visit_types
    //       ↓
    // forms
    //
    // =========================================================

    public void deleteForm(Long formId) {

        // ---------------------------------------------------------
        // Make sure the form exists
        // ---------------------------------------------------------

        Form form = formRepository.findById(formId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Form not found with id: " + formId
                        )
                );

        // ---------------------------------------------------------
        // Capture information BEFORE deleting
        // ---------------------------------------------------------

        Long domainId =
                form.getDomain() != null
                        ? form.getDomain().getId()
                        : null;

        String formName = form.getName();

        User admin = getAuthenticatedUser();

        // ---------------------------------------------------------
        // 1. Delete submission values belonging to submissions
        //    of this form
        // ---------------------------------------------------------

        jdbcTemplate.update(
                """
                DELETE FROM submission_values
                WHERE submission_id IN (
                    SELECT id
                    FROM form_submissions
                    WHERE form_id = ?
                )
                """,
                formId
        );

        // ---------------------------------------------------------
        // 2. Delete form submissions
        // ---------------------------------------------------------

        jdbcTemplate.update(
                """
                DELETE FROM form_submissions
                WHERE form_id = ?
                """,
                formId
        );

        // ---------------------------------------------------------
        // 3. Delete field rules
        // ---------------------------------------------------------

        jdbcTemplate.update(
                """
                DELETE FROM field_rules
                WHERE form_id = ?
                """,
                formId
        );

        // ---------------------------------------------------------
        // 4. Delete FormField ↔ VisitType mappings
        // ---------------------------------------------------------

        jdbcTemplate.update(
                """
                DELETE FROM form_field_visit_types
                WHERE form_field_id IN (
                    SELECT id
                    FROM form_fields
                    WHERE form_id = ?
                )
                """,
                formId
        );

        // ---------------------------------------------------------
        // 5. Delete field options
        // ---------------------------------------------------------

        jdbcTemplate.update(
                """
                DELETE FROM field_options
                WHERE field_id IN (
                    SELECT id
                    FROM form_fields
                    WHERE form_id = ?
                )
                """,
                formId
        );

        // ---------------------------------------------------------
        // 6. Delete form fields
        // ---------------------------------------------------------

        jdbcTemplate.update(
                """
                DELETE FROM form_fields
                WHERE form_id = ?
                """,
                formId
        );

        // ---------------------------------------------------------
        // 7. Delete Form ↔ VisitType mappings
        // ---------------------------------------------------------

        jdbcTemplate.update(
                """
                DELETE FROM form_visit_types
                WHERE form_id = ?
                """,
                formId
        );

        // ---------------------------------------------------------
        // 8. Finally delete the form itself
        // ---------------------------------------------------------

        jdbcTemplate.update(
                """
                DELETE FROM forms
                WHERE id = ?
                """,
                formId
        );

        // =====================================================
        // NOTIFY USERS AFTER SUCCESSFUL DELETE
        // =====================================================

        if (domainId != null) {

            notificationService.notifyDomainUsers(
                    domainId,
                    admin,
                    "FORM_DELETED",
                    "Form Deleted",
                    "The form '" + formName
                            + "' was deleted by an administrator.",
                    "FORM",
                    formId
            );
        }
    }

    // =========================================================
    // SAVE FORM ↔ VISIT TYPE MAPPINGS
    // =========================================================

    private void saveVisitTypeMappings(
            Form form,
            List<Long> visitTypeIds,
            Domain domain
    ) {

        if (visitTypeIds == null ||
                visitTypeIds.isEmpty()) {

            throw new IllegalArgumentException(
                    "At least one visit type must be selected"
            );
        }

        List<Long> uniqueVisitTypeIds =
                visitTypeIds.stream()
                        .distinct()
                        .toList();

        List<VisitType> visitTypes =
                visitTypeRepository.findAllById(
                        uniqueVisitTypeIds
                );

        if (visitTypes.size() !=
                uniqueVisitTypeIds.size()) {

            throw new ResourceNotFoundException(
                    "One or more selected visit types were not found"
            );
        }

        List<FormVisitType> mappings =
                new ArrayList<>();

        for (VisitType visitType : visitTypes) {

            // -----------------------------------------------------
            // Visit Type must belong to selected domain
            // -----------------------------------------------------

            if (!visitType.getDomain().getId()
                    .equals(domain.getId())) {

                throw new IllegalArgumentException(
                        "Visit type '"
                                + visitType.getName()
                                + "' does not belong to domain '"
                                + domain.getName()
                                + "'"
                );
            }

            // -----------------------------------------------------
            // Only active Visit Types can be associated
            // -----------------------------------------------------

            if (!Boolean.TRUE.equals(
                    visitType.getActive()
            )) {

                throw new IllegalArgumentException(
                        "Visit type '"
                                + visitType.getName()
                                + "' is inactive"
                );
            }

            FormVisitTypeId mappingId =
                    new FormVisitTypeId(
                            form.getId(),
                            visitType.getId()
                    );

            FormVisitType mapping =
                    new FormVisitType();

            mapping.setId(mappingId);
            mapping.setForm(form);
            mapping.setVisitType(visitType);

            mappings.add(mapping);
        }

        formVisitTypeRepository.saveAll(
                mappings
        );
    }

    // =========================================================
    // GET FORM ↔ VISIT TYPE MAPPINGS
    // =========================================================

    @Transactional(readOnly = true)
    public List<FormVisitType> getFormVisitTypes(
            Long formId
    ) {

        if (!formRepository.existsById(formId)) {

            throw new ResourceNotFoundException(
                    "Form not found with id: " + formId
            );
        }

        return formVisitTypeRepository
                .findByForm_Id(formId);
    }

    // =========================================================
    // GET AUTHENTICATED ADMIN
    // =========================================================

    private User getAuthenticatedUser() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated()) {

            throw new IllegalStateException(
                    "Authentication required"
            );
        }

        return userRepository
                .findByEmail(authentication.getName())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Authenticated user not found"
                        )
                );
    }

    // =========================================================
    // VALIDATION
    // =========================================================

    private void validateRequest(
            CreateFormRequest request
    ) {

        if (request == null) {

            throw new IllegalArgumentException(
                    "Form request cannot be null"
            );
        }

        if (request.getDomainId() == null) {

            throw new IllegalArgumentException(
                    "Domain is required"
            );
        }

        if (request.getName() == null ||
                request.getName().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Form name is required"
            );
        }

        if (request.getCode() == null ||
                request.getCode().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Form code is required"
            );
        }

        if (request.getName().trim().length() > 150) {

            throw new IllegalArgumentException(
                    "Form name cannot exceed 150 characters"
            );
        }

        if (request.getCode().trim().length() > 100) {

            throw new IllegalArgumentException(
                    "Form code cannot exceed 100 characters"
            );
        }
    }
}