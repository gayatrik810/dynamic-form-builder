package com.example.dfbbackend.service;

import com.example.dfbbackend.dto.CreateFormFieldRequest;
import com.example.dfbbackend.dto.FormFieldResponse;
import com.example.dfbbackend.entity.Form;
import com.example.dfbbackend.entity.FormField;
import com.example.dfbbackend.entity.FormFieldVisitType;
import com.example.dfbbackend.entity.FormFieldVisitTypeId;
import com.example.dfbbackend.entity.FormVisitType;
import com.example.dfbbackend.entity.User;
import com.example.dfbbackend.repository.FieldOptionRepository;
import com.example.dfbbackend.repository.FieldRuleRepository;
import com.example.dfbbackend.repository.FormFieldRepository;
import com.example.dfbbackend.repository.FormFieldVisitTypeRepository;
import com.example.dfbbackend.repository.FormRepository;
import com.example.dfbbackend.repository.FormVisitTypeRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class FormFieldService {

    private final FormFieldRepository formFieldRepository;
    private final FormRepository formRepository;
    private final FieldOptionRepository fieldOptionRepository;
    private final FieldRuleRepository fieldRuleRepository;
    private final FormFieldVisitTypeRepository formFieldVisitTypeRepository;
    private final FormVisitTypeRepository formVisitTypeRepository;

    private final NotificationService notificationService;
    private final com.example.dfbbackend.repository.UserRepository userRepository;


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public FormFieldService(
            FormFieldRepository formFieldRepository,
            FormRepository formRepository,
            FieldOptionRepository fieldOptionRepository,
            FieldRuleRepository fieldRuleRepository,
            FormFieldVisitTypeRepository formFieldVisitTypeRepository,
            FormVisitTypeRepository formVisitTypeRepository,
            NotificationService notificationService,
            com.example.dfbbackend.repository.UserRepository userRepository
    ) {

        this.formFieldRepository =
                formFieldRepository;

        this.formRepository =
                formRepository;

        this.fieldOptionRepository =
                fieldOptionRepository;

        this.fieldRuleRepository =
                fieldRuleRepository;

        this.formFieldVisitTypeRepository =
                formFieldVisitTypeRepository;

        this.formVisitTypeRepository =
                formVisitTypeRepository;

        this.notificationService =
                notificationService;

        this.userRepository =
                userRepository;
    }


    // =========================================================
    // GET ALL FIELDS FOR FORM
    // =========================================================

    @Transactional
    public List<FormFieldResponse> getFieldsByForm(
            Long formId
    ) {

        // Verify form exists
        formRepository.findById(formId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Form not found with id: " + formId
                        )
                );


        List<FormField> fields =
                formFieldRepository
                        .findByFormIdOrderByDisplayOrderAsc(
                                formId
                        );


        return fields.stream()
                .map(this::toResponse)
                .toList();
    }


    // =========================================================
    // GET SINGLE FIELD
    // =========================================================

    @Transactional
    public FormFieldResponse getFieldById(
            Long fieldId
    ) {

        FormField field =
                formFieldRepository.findById(fieldId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Form field not found with id: "
                                                + fieldId
                                )
                        );

        return toResponse(field);
    }


    // =========================================================
    // CREATE FIELD
    // =========================================================

    public FormFieldResponse createField(
            Long formId,
            CreateFormFieldRequest request
    ) {

        // -----------------------------------------------------
        // Validate form
        // -----------------------------------------------------

        Form form =
                formRepository.findById(formId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Form not found with id: "
                                                + formId
                                )
                        );


        // -----------------------------------------------------
        // Validate request
        // -----------------------------------------------------

        if (request == null) {
            throw new IllegalArgumentException(
                    "Field data is required"
            );
        }


        if (request.getFieldKey() == null ||
                request.getFieldKey().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Field key is required"
            );
        }


        if (request.getLabel() == null ||
                request.getLabel().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Field label is required"
            );
        }


        if (request.getFieldType() == null ||
                request.getFieldType().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Field type is required"
            );
        }


        String fieldKey =
                request.getFieldKey().trim();


        // -----------------------------------------------------
        // Check duplicate field key
        // -----------------------------------------------------

        if (formFieldRepository.existsByFormIdAndFieldKey(
                formId,
                fieldKey
        )) {

            throw new IllegalArgumentException(
                    "A field with key '" +
                            fieldKey +
                            "' already exists in this form."
            );
        }


        // -----------------------------------------------------
        // Create entity
        // -----------------------------------------------------

        FormField field =
                new FormField();

        field.setForm(form);

        field.setFieldKey(fieldKey);

        field.setLabel(
                request.getLabel().trim()
        );

        field.setFieldType(
                request.getFieldType().trim()
        );

        field.setRequired(
                request.getRequired() != null
                        ? request.getRequired()
                        : false
        );


        // -----------------------------------------------------
        // Display order
        // -----------------------------------------------------

        Integer displayOrder =
                request.getDisplayOrder();

        if (displayOrder == null) {

            List<FormField> existingFields =
                    formFieldRepository
                            .findByFormIdOrderByDisplayOrderAsc(
                                    formId
                            );

            displayOrder =
                    existingFields.size() + 1;
        }

        field.setDisplayOrder(displayOrder);


        // -----------------------------------------------------
        // Placeholder
        // -----------------------------------------------------

        if (request.getPlaceholder() != null &&
                !request.getPlaceholder()
                        .trim()
                        .isEmpty()) {

            field.setPlaceholder(
                    request.getPlaceholder().trim()
            );

        } else {

            field.setPlaceholder(null);
        }


        // -----------------------------------------------------
        // Validation rule
        // -----------------------------------------------------

        if (request.getValidationRule() != null &&
                !request.getValidationRule()
                        .trim()
                        .isEmpty()) {

            field.setValidationRule(
                    request.getValidationRule().trim()
            );

        } else {

            field.setValidationRule(null);
        }


        // -----------------------------------------------------
        // Active
        // -----------------------------------------------------

        field.setActive(true);


        // -----------------------------------------------------
        // Save field
        // -----------------------------------------------------

        FormField savedField =
                formFieldRepository.save(field);


        // =====================================================
        // Map new field to all visit types of this form
        // =====================================================

        List<FormVisitType> formVisitTypes =
                formVisitTypeRepository
                        .findByForm_Id(formId);


        for (FormVisitType formVisitType :
                formVisitTypes) {

            if (formVisitType.getVisitType() == null) {
                continue;
            }


            Long visitTypeId =
                    formVisitType
                            .getVisitType()
                            .getId();


            FormFieldVisitTypeId mappingId =
                    new FormFieldVisitTypeId(
                            savedField.getId(),
                            visitTypeId
                    );


            if (!formFieldVisitTypeRepository
                    .existsById(mappingId)) {

                FormFieldVisitType mapping =
                        new FormFieldVisitType();

                mapping.setId(mappingId);

                mapping.setFormField(
                        savedField
                );

                mapping.setVisitType(
                        formVisitType.getVisitType()
                );


                formFieldVisitTypeRepository.save(
                        mapping
                );
            }
        }


        // =====================================================
        // NOTIFY DOMAIN USERS
        // =====================================================

        User admin =
                getAuthenticatedUser();

        if (form.getDomain() != null) {

            notificationService.notifyDomainUsers(
                    form.getDomain().getId(),
                    admin,
                    "FORM_FIELD_CREATED",
                    "New Form Field Added",
                    "A new field '"
                            + savedField.getLabel()
                            + "' was added to the form '"
                            + form.getName()
                            + "'.",
                    "FORM_FIELD",
                    savedField.getId()
            );
        }


        return toResponse(savedField);
    }


    // =========================================================
    // UPDATE FIELD
    // =========================================================

    public FormFieldResponse updateField(
            Long fieldId,
            CreateFormFieldRequest request
    ) {

        FormField field =
                formFieldRepository.findById(fieldId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Form field not found with id: "
                                                + fieldId
                                )
                        );


        if (request == null) {

            throw new IllegalArgumentException(
                    "Field data is required"
            );
        }


        // -----------------------------------------------------
        // Field key
        // -----------------------------------------------------

        if (request.getFieldKey() == null ||
                request.getFieldKey().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Field key is required"
            );
        }


        String newFieldKey =
                request.getFieldKey().trim();


        // -----------------------------------------------------
        // Check duplicate key
        // -----------------------------------------------------

        List<FormField> existingFields =
                formFieldRepository
                        .findByFormIdOrderByDisplayOrderAsc(
                                field.getForm().getId()
                        );


        for (FormField existing :
                existingFields) {

            if (!existing.getId().equals(fieldId) &&
                    existing.getFieldKey()
                            .equalsIgnoreCase(newFieldKey)) {

                throw new IllegalArgumentException(
                        "A field with key '" +
                                newFieldKey +
                                "' already exists in this form."
                );
            }
        }


        // -----------------------------------------------------
        // Label
        // -----------------------------------------------------

        if (request.getLabel() == null ||
                request.getLabel().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Field label is required"
            );
        }


        // -----------------------------------------------------
        // Field type
        // -----------------------------------------------------

        if (request.getFieldType() == null ||
                request.getFieldType().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Field type is required"
            );
        }


        // -----------------------------------------------------
        // Update values
        // -----------------------------------------------------

        field.setFieldKey(newFieldKey);

        field.setLabel(
                request.getLabel().trim()
        );

        field.setFieldType(
                request.getFieldType().trim()
        );

        field.setRequired(
                request.getRequired() != null
                        ? request.getRequired()
                        : false
        );


        if (request.getDisplayOrder() != null) {

            field.setDisplayOrder(
                    request.getDisplayOrder()
            );
        }


        if (request.getPlaceholder() != null &&
                !request.getPlaceholder()
                        .trim()
                        .isEmpty()) {

            field.setPlaceholder(
                    request.getPlaceholder().trim()
            );

        } else {

            field.setPlaceholder(null);
        }


        if (request.getValidationRule() != null &&
                !request.getValidationRule()
                        .trim()
                        .isEmpty()) {

            field.setValidationRule(
                    request.getValidationRule().trim()
            );

        } else {

            field.setValidationRule(null);
        }


        FormField savedField =
                formFieldRepository.save(field);


        // =====================================================
        // NOTIFY DOMAIN USERS
        // =====================================================

        Form form =
                savedField.getForm();

        User admin =
                getAuthenticatedUser();

        if (form != null &&
                form.getDomain() != null) {

            notificationService.notifyDomainUsers(
                    form.getDomain().getId(),
                    admin,
                    "FORM_FIELD_UPDATED",
                    "Form Field Updated",
                    "The field '"
                            + savedField.getLabel()
                            + "' in the form '"
                            + form.getName()
                            + "' was updated by an administrator.",
                    "FORM_FIELD",
                    savedField.getId()
            );
        }


        return toResponse(savedField);
    }


    // =========================================================
    // DEACTIVATE FIELD
    // =========================================================

    public FormFieldResponse deactivateField(
            Long fieldId
    ) {

        FormField field =
                formFieldRepository.findById(fieldId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Form field not found with id: "
                                                + fieldId
                                )
                        );


        field.setActive(false);


        FormField savedField =
                formFieldRepository.save(field);


        // =====================================================
        // NOTIFY DOMAIN USERS
        // =====================================================

        Form form =
                savedField.getForm();

        User admin =
                getAuthenticatedUser();

        if (form != null &&
                form.getDomain() != null) {

            notificationService.notifyDomainUsers(
                    form.getDomain().getId(),
                    admin,
                    "FORM_FIELD_DEACTIVATED",
                    "Form Field Deactivated",
                    "The field '"
                            + savedField.getLabel()
                            + "' in the form '"
                            + form.getName()
                            + "' has been deactivated.",
                    "FORM_FIELD",
                    savedField.getId()
            );
        }


        return toResponse(savedField);
    }


    // =========================================================
    // ACTIVATE FIELD
    // =========================================================

    public FormFieldResponse activateField(
            Long fieldId
    ) {

        FormField field =
                formFieldRepository.findById(fieldId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Form field not found with id: "
                                                + fieldId
                                )
                        );


        field.setActive(true);


        FormField savedField =
                formFieldRepository.save(field);


        // =====================================================
        // NOTIFY DOMAIN USERS
        // =====================================================

        Form form =
                savedField.getForm();

        User admin =
                getAuthenticatedUser();

        if (form != null &&
                form.getDomain() != null) {

            notificationService.notifyDomainUsers(
                    form.getDomain().getId(),
                    admin,
                    "FORM_FIELD_ACTIVATED",
                    "Form Field Activated",
                    "The field '"
                            + savedField.getLabel()
                            + "' in the form '"
                            + form.getName()
                            + "' is now active.",
                    "FORM_FIELD",
                    savedField.getId()
            );
        }


        return toResponse(savedField);
    }


    // =========================================================
    // DELETE FIELD
    // =========================================================

    public void deleteField(
            Long fieldId
    ) {

        FormField field =
                formFieldRepository.findById(fieldId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Form field not found with id: "
                                                + fieldId
                                )
                        );


        // -----------------------------------------------------
        // Capture information BEFORE deletion
        // -----------------------------------------------------

        Form form =
                field.getForm();

        Long domainId =
                form != null &&
                        form.getDomain() != null
                        ? form.getDomain().getId()
                        : null;

        String fieldLabel =
                field.getLabel();

        String formName =
                form != null
                        ? form.getName()
                        : "form";


        // -----------------------------------------------------
        // Delete rules where this field is source
        // -----------------------------------------------------

        fieldRuleRepository.deleteBySourceField_Id(
                fieldId
        );


        // -----------------------------------------------------
        // Delete rules where this field is target
        // -----------------------------------------------------

        fieldRuleRepository.deleteByTargetField_Id(
                fieldId
        );


        // -----------------------------------------------------
        // Delete visit type mappings
        // -----------------------------------------------------

        formFieldVisitTypeRepository.deleteByFormField_Id(
                fieldId
        );


        // -----------------------------------------------------
        // Delete options
        // -----------------------------------------------------

        fieldOptionRepository.deleteByField_Id(
                fieldId
        );


        // -----------------------------------------------------
        // Finally delete field
        // -----------------------------------------------------

        formFieldRepository.delete(field);


        // =====================================================
        // NOTIFY DOMAIN USERS
        // =====================================================

        if (domainId != null) {

            User admin =
                    getAuthenticatedUser();

            notificationService.notifyDomainUsers(
                    domainId,
                    admin,
                    "FORM_FIELD_DELETED",
                    "Form Field Deleted",
                    "The field '"
                            + fieldLabel
                            + "' was deleted from the form '"
                            + formName
                            + "'.",
                    "FORM_FIELD",
                    fieldId
            );
        }
    }


    // =========================================================
    // GET AUTHENTICATED USER
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
                        new IllegalStateException(
                                "Authenticated user not found"
                        )
                );
    }


    // =========================================================
    // RESPONSE MAPPER
    // =========================================================

    private FormFieldResponse toResponse(
            FormField field
    ) {

        FormFieldResponse response =
                new FormFieldResponse();


        response.setId(
                field.getId()
        );

        response.setFieldKey(
                field.getFieldKey()
        );

        response.setLabel(
                field.getLabel()
        );

        response.setFieldType(
                field.getFieldType()
        );

        response.setRequired(
                field.getRequired()
        );

        response.setDisplayOrder(
                field.getDisplayOrder()
        );

        response.setPlaceholder(
                field.getPlaceholder()
        );

        response.setValidationRule(
                field.getValidationRule()
        );

        response.setActive(
                field.getActive()
        );


        // -----------------------------------------------------
        // Options
        // -----------------------------------------------------

        if (field.getOptions() != null) {

            response.setOptions(
                    field.getOptions()
                            .stream()
                            .filter(option ->
                                    Boolean.TRUE.equals(
                                            option.getActive()
                                    )
                            )
                            .sorted(
                                    java.util.Comparator
                                            .comparing(
                                                    option ->
                                                            option
                                                                    .getDisplayOrder()
                                                                    != null
                                                                    ? option.getDisplayOrder()
                                                                    : 0
                                            )
                            )
                            .map(option -> {

                                com.example.dfbbackend.dto.FieldOptionResponse
                                        optionResponse =
                                        new com.example.dfbbackend.dto.FieldOptionResponse();

                                optionResponse.setId(
                                        option.getId()
                                );

                                optionResponse.setLabel(
                                        option.getLabel()
                                );

                                optionResponse.setValue(
                                        option.getValue()
                                );

                                optionResponse.setDisplayOrder(
                                        option.getDisplayOrder()
                                );

                                optionResponse.setActive(
                                        option.getActive()
                                );

                                return optionResponse;

                            })
                            .toList()
            );
        }


        return response;
    }
}