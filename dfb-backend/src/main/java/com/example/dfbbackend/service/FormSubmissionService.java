package com.example.dfbbackend.service;

import com.example.dfbbackend.dto.FormSubmissionRequest;
import com.example.dfbbackend.dto.FormSubmissionResponse;
import com.example.dfbbackend.dto.SubmissionDetailResponse;
import com.example.dfbbackend.dto.SubmissionFieldValueResponse;
import com.example.dfbbackend.dto.SubmissionHistoryResponse;
import com.example.dfbbackend.entity.FieldOption;
import com.example.dfbbackend.entity.FieldRule;
import com.example.dfbbackend.entity.Form;
import com.example.dfbbackend.entity.FormField;
import com.example.dfbbackend.entity.FormFieldVisitType;
import com.example.dfbbackend.entity.FormSubmission;
import com.example.dfbbackend.entity.SubmissionValue;
import com.example.dfbbackend.entity.User;
import com.example.dfbbackend.entity.VisitType;
import com.example.dfbbackend.repository.FieldRuleRepository;
import com.example.dfbbackend.repository.FormFieldVisitTypeRepository;
import com.example.dfbbackend.repository.FormRepository;
import com.example.dfbbackend.repository.FormSubmissionRepository;
import com.example.dfbbackend.repository.SubmissionValueRepository;
import com.example.dfbbackend.repository.UserRepository;
import com.example.dfbbackend.repository.VisitTypeRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FormSubmissionService {

    private final FormRepository formRepository;
    private final FormFieldVisitTypeRepository formFieldVisitTypeRepository;
    private final FormSubmissionRepository formSubmissionRepository;
    private final SubmissionValueRepository submissionValueRepository;
    private final UserRepository userRepository;
    private final FieldRuleRepository fieldRuleRepository;
    private final VisitTypeRepository visitTypeRepository;
    private final NotificationService notificationService;


    public FormSubmissionService(
            FormRepository formRepository,
            FormFieldVisitTypeRepository formFieldVisitTypeRepository,
            FormSubmissionRepository formSubmissionRepository,
            SubmissionValueRepository submissionValueRepository,
            UserRepository userRepository,
            FieldRuleRepository fieldRuleRepository,
            VisitTypeRepository visitTypeRepository,
            NotificationService notificationService
    ) {

        this.formRepository = formRepository;
        this.formFieldVisitTypeRepository =
                formFieldVisitTypeRepository;
        this.formSubmissionRepository =
                formSubmissionRepository;
        this.submissionValueRepository =
                submissionValueRepository;
        this.userRepository =
                userRepository;
        this.fieldRuleRepository =
                fieldRuleRepository;
        this.visitTypeRepository =
                visitTypeRepository;
        this.notificationService =
                notificationService;
    }


    // =====================================================
    // SUBMIT FORM
    // =====================================================

    @Transactional
    public FormSubmissionResponse submit(
            FormSubmissionRequest request,
            Authentication authentication
    ) {

        // =================================================
        // VALIDATE REQUEST
        // =================================================

        if (request == null) {
            throw new IllegalArgumentException(
                    "Submission request is required"
            );
        }

        if (request.getFormId() == null) {
            throw new IllegalArgumentException(
                    "formId is required"
            );
        }

        if (request.getVisitTypeId() == null) {
            throw new IllegalArgumentException(
                    "visitTypeId is required"
            );
        }

        if (request.getValues() == null) {
            throw new IllegalArgumentException(
                    "Values cannot be empty"
            );
        }


        // =================================================
        // FIND FORM
        // =================================================

        Form form =
                formRepository
                        .findById(
                                request.getFormId()
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Form not found"
                                )
                        );

        if (!Boolean.TRUE.equals(
                form.getActive()
        )) {

            throw new IllegalArgumentException(
                    "Form is inactive"
            );
        }


        // =================================================
        // FIND VISIT TYPE
        // =================================================

        VisitType visitType =
                visitTypeRepository
                        .findById(
                                request.getVisitTypeId()
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Visit type not found"
                                )
                        );

        if (!Boolean.TRUE.equals(
                visitType.getActive()
        )) {

            throw new IllegalArgumentException(
                    "Visit type is inactive"
            );
        }


        // =================================================
        // VALIDATE FORM DOMAIN AND VISIT TYPE DOMAIN
        // =================================================

        if (!form.getDomain()
                .getId()
                .equals(
                        visitType
                                .getDomain()
                                .getId()
                )) {

            throw new IllegalArgumentException(
                    "Visit type does not belong to the form domain"
            );
        }


        // =================================================
        // GET LOGGED-IN USER
        // =================================================

        User user =
                getAuthenticatedUser(
                        authentication
                );

        if (!Boolean.TRUE.equals(
                user.getActive()
        )) {

            throw new IllegalArgumentException(
                    "User is inactive"
            );
        }


        // =================================================
        // VALIDATE USER DOMAIN
        // =================================================

        if (user.getDomain() == null) {

            throw new IllegalArgumentException(
                    "User has no domain assigned"
            );
        }


        // =================================================
        // CHECK USER DOMAIN STATUS
        // =================================================

        if (!Boolean.TRUE.equals(
                user.getDomain().getActive()
        )) {

            throw new IllegalArgumentException(
                    "User's domain is inactive. New submissions are not allowed"
            );
        }


        // =================================================
        // VALIDATE USER DOMAIN MATCH
        // =================================================

        if (!user.getDomain()
                .getId()
                .equals(
                        form.getDomain()
                                .getId()
                )) {

            throw new IllegalArgumentException(
                    "User is not authorized for this domain"
            );
        }


        // =================================================
        // GET FIELDS FOR VISIT TYPE
        // =================================================

        List<FormField> fields =
                formFieldVisitTypeRepository
                        .findByVisitType_Id(
                                visitType.getId()
                        )
                        .stream()
                        .map(
                                FormFieldVisitType::getFormField
                        )
                        .filter(formField ->
                                formField
                                        .getForm()
                                        .getId()
                                        .equals(
                                                form.getId()
                                        )
                        )
                        .filter(formField ->
                                Boolean.TRUE.equals(
                                        formField.getActive()
                                )
                        )
                        .sorted(
                                Comparator.comparing(
                                        FormField::getDisplayOrder
                                )
                        )
                        .toList();

        if (fields.isEmpty()) {

            throw new IllegalArgumentException(
                    "No active fields are assigned to this visit type"
            );
        }


        // =================================================
        // SUBMITTED VALUES
        // =================================================

        Map<String, Object> submittedValues =
                request.getValues();


        // =================================================
        // LOAD FIELD RULES
        // =================================================

        List<FieldRule> rules =
                fieldRuleRepository
                        .findByForm_IdAndVisitType_Id(
                                form.getId(),
                                visitType.getId()
                        );


        // =================================================
        // CREATE FIELD MAPS
        // =================================================

        Map<String, FormField> fieldsByKey =
                new HashMap<>();

        Map<Long, FormField> fieldsById =
                new HashMap<>();

        for (FormField field : fields) {

            fieldsByKey.put(
                    field.getFieldKey(),
                    field
            );

            fieldsById.put(
                    field.getId(),
                    field
            );
        }


        // =================================================
        // VALIDATE UNKNOWN FIELDS
        // =================================================

        for (String submittedKey :
                submittedValues.keySet()) {

            if (!fieldsByKey.containsKey(
                    submittedKey
            )) {

                throw new IllegalArgumentException(
                        "Field is not available for this visit type: "
                                + submittedKey
                );
            }
        }


        // =================================================
        // VALIDATE ALL FIELD VALUES
        // =================================================

        for (FormField field : fields) {

            String fieldKey =
                    field.getFieldKey();


            // =============================================
            // SYSTEM FIELD
            // =============================================

            if ("SYSTEM".equalsIgnoreCase(
                    field.getFieldType()
            )) {

                if (submittedValues.containsKey(
                        fieldKey
                )) {

                    throw new IllegalArgumentException(
                            "System field cannot be provided: "
                                    + fieldKey
                    );
                }

                continue;
            }


            Object value =
                    submittedValues.get(
                            fieldKey
                    );


            // =============================================
            // CONDITIONAL HIDDEN
            // =============================================

            boolean conditionallyHidden =
                    isConditionallyHidden(
                            field,
                            rules,
                            fieldsById,
                            submittedValues
                    );

            if (conditionallyHidden) {

                if (hasValue(value)) {

                    throw new IllegalArgumentException(
                            "Field not applicable: "
                                    + fieldKey
                    );
                }

                continue;
            }


            // =============================================
            // REQUIRED VALIDATION
            // =============================================

            boolean required =
                    Boolean.TRUE.equals(
                            field.getRequired()
                    )
                            ||
                            isConditionallyRequired(
                                    field,
                                    rules,
                                    fieldsById,
                                    submittedValues
                            );

            if (required &&
                    !hasValue(value)) {

                throw new IllegalArgumentException(
                        "Required field missing: "
                                + fieldKey
                );
            }


            // =============================================
            // OPTIONAL EMPTY FIELD
            // =============================================

            if (!hasValue(value)) {
                continue;
            }


            // =============================================
            // VALIDATE FIELD VALUE
            // =============================================

            validateFieldValue(
                    field,
                    value
            );
        }


        // =================================================
        // CREATE SUBMISSION
        // =================================================

        LocalDateTime now =
                LocalDateTime.now();

        FormSubmission submission =
                new FormSubmission();

        submission.setForm(
                form
        );

        submission.setVisitType(
                visitType
        );

        submission.setSubmittedBy(
                user
        );

        submission.setSubmittedAt(
                now
        );

        submission.setUpdatedAt(
                now
        );


        submission =
                formSubmissionRepository
                        .save(
                                submission
                        );


        // =================================================
        // SAVE SUBMISSION VALUES
        // =================================================

        for (FormField field : fields) {

            String fieldKey =
                    field.getFieldKey();

            Object value;


            // =============================================
            // SYSTEM FIELD VALUE
            // =============================================

            if ("SYSTEM".equalsIgnoreCase(
                    field.getFieldType()
            )) {

                value =
                        user.getName();

            } else {

                value =
                        submittedValues.get(
                                fieldKey
                        );
            }


            // =============================================
            // DON'T SAVE EMPTY VALUES
            // =============================================

            if (!hasValue(value)) {
                continue;
            }


            // =============================================
            // CREATE SUBMISSION VALUE
            // =============================================

            SubmissionValue submissionValue =
                    new SubmissionValue();

            submissionValue.setSubmission(
                    submission
            );

            submissionValue.setField(
                    field
            );


            setValue(
                    submissionValue,
                    field.getFieldType(),
                    value
            );


            submissionValueRepository.save(
                    submissionValue
            );
        }


        // =================================================
        // CREATE ADMIN NOTIFICATION
        // =================================================

        notificationService.notifyAdmins(
                user,
                "FORM_SUBMISSION",
                "New Form Submission",
                user.getName()
                        + " submitted a "
                        + visitType.getName()
                        + ".",
                "SUBMISSION",
                submission.getId()
        );


        // =================================================
        // RETURN RESPONSE
        // =================================================

        return new FormSubmissionResponse(
                submission.getId(),
                form.getId(),
                visitType.getId(),
                user.getId(),
                submission.getSubmittedAt(),
                "Form submitted successfully"
        );
    }


    // =====================================================
    // GET MY SUBMISSION HISTORY
    // =====================================================

    @Transactional(readOnly = true)
    public List<SubmissionHistoryResponse>
    getMySubmissions(
            Authentication authentication
    ) {

        User user =
                getAuthenticatedUser(
                        authentication
                );

        return formSubmissionRepository
                .findBySubmittedBy_IdOrderBySubmittedAtDesc(
                        user.getId()
                )
                .stream()
                .map(submission ->
                        new SubmissionHistoryResponse(
                                submission.getId(),
                                submission.getForm().getId(),
                                submission.getForm().getName(),
                                submission.getVisitType().getId(),
                                submission.getVisitType().getName(),
                                submission.getSubmittedBy().getId(),
                                submission.getSubmittedAt()
                        )
                )
                .toList();
    }


    // =====================================================
    // GET ONE SUBMISSION DETAIL
    // USER CAN ONLY VIEW THEIR OWN SUBMISSION
    // =====================================================

    @Transactional(readOnly = true)
    public SubmissionDetailResponse
    getMySubmissionById(
            Long submissionId,
            Authentication authentication
    ) {

        User user =
                getAuthenticatedUser(
                        authentication
                );


        FormSubmission submission =
                formSubmissionRepository
                        .findByIdAndSubmittedBy_Id(
                                submissionId,
                                user.getId()
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Submission not found or access denied"
                                )
                        );


        // =================================================
        // GET VALUES
        // IMPORTANT:
        // THIS MATCHES YOUR REPOSITORY METHOD EXACTLY
        // =================================================

        List<SubmissionValue> submissionValues =
                submissionValueRepository
                        .findBySubmission_IdOrderByField_DisplayOrderAsc(
                                submissionId
                        );


        List<SubmissionFieldValueResponse> values =
                submissionValues
                        .stream()
                        .map(submissionValue ->
                                new SubmissionFieldValueResponse(
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

                                        getSubmissionValue(
                                                submissionValue
                                        )
                                )
                        )
                        .toList();


        return new SubmissionDetailResponse(

                submission.getId(),

                submission.getForm()
                        .getId(),

                submission.getForm()
                        .getName(),

                submission.getVisitType()
                        .getId(),

                submission.getVisitType()
                        .getName(),

                submission.getSubmittedBy()
                        .getId(),

                submission.getSubmittedAt(),

                values
        );
    }


    // =====================================================
    // GET AUTHENTICATED USER
    // =====================================================

    private User getAuthenticatedUser(
            Authentication authentication
    ) {

        if (authentication == null ||
                authentication.getName() == null) {

            throw new IllegalArgumentException(
                    "Authenticated user is required"
            );
        }


        return userRepository
                .findByEmail(
                        authentication.getName()
                )
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Logged-in user not found"
                        )
                );
    }


    // =====================================================
    // CONVERT SUBMISSION VALUE TO OBJECT
    // =====================================================

    private Object getSubmissionValue(
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


    // =====================================================
    // CHECK WHETHER VALUE EXISTS
    // =====================================================

    private boolean hasValue(
            Object value
    ) {

        if (value == null) {
            return false;
        }

        if (value instanceof String stringValue) {

            return !stringValue
                    .trim()
                    .isEmpty();
        }

        if (value instanceof List<?> listValue) {

            return !listValue.isEmpty();
        }

        return true;
    }


    // =====================================================
    // CONDITIONAL HIDDEN CHECK
    // =====================================================

    private boolean isConditionallyHidden(
            FormField targetField,
            List<FieldRule> rules,
            Map<Long, FormField> fieldsById,
            Map<String, Object> submittedValues
    ) {

        for (FieldRule rule : rules) {

            if (!"SHOW".equalsIgnoreCase(
                    rule.getAction()
            )) {
                continue;
            }

            if (!rule.getTargetField()
                    .getId()
                    .equals(
                            targetField.getId()
                    )) {
                continue;
            }


            FormField sourceField =
                    fieldsById.get(
                            rule.getSourceField()
                                    .getId()
                    );


            if (sourceField == null) {
                continue;
            }


            Object sourceValue =
                    submittedValues.get(
                            sourceField.getFieldKey()
                    );


            if (!hasValue(sourceValue)) {
                return true;
            }


            if (!evaluateRule(
                    rule,
                    sourceValue
            )) {

                return true;
            }
        }

        return false;
    }


    // =====================================================
    // CONDITIONAL REQUIRED CHECK
    // =====================================================

    private boolean isConditionallyRequired(
            FormField targetField,
            List<FieldRule> rules,
            Map<Long, FormField> fieldsById,
            Map<String, Object> submittedValues
    ) {

        for (FieldRule rule : rules) {

            if (!"SHOW".equalsIgnoreCase(
                    rule.getAction()
            )) {
                continue;
            }

            if (!rule.getTargetField()
                    .getId()
                    .equals(
                            targetField.getId()
                    )) {
                continue;
            }


            FormField sourceField =
                    fieldsById.get(
                            rule.getSourceField()
                                    .getId()
                    );


            if (sourceField == null) {
                continue;
            }


            Object sourceValue =
                    submittedValues.get(
                            sourceField.getFieldKey()
                    );


            if (!hasValue(sourceValue)) {
                continue;
            }


            if (evaluateRule(
                    rule,
                    sourceValue
            )) {

                return true;
            }
        }

        return false;
    }


    // =====================================================
    // RULE EVALUATION
    // =====================================================

    private boolean evaluateRule(
            FieldRule rule,
            Object sourceValue
    ) {

        String operator =
                rule.getOperator();

        String expectedValue =
                rule.getExpectedValue();


        // =================================================
        // MULTI SELECT
        // =================================================

        if (sourceValue instanceof List<?> selectedValues) {

            if ("EQUALS".equalsIgnoreCase(
                    operator
            )) {

                return selectedValues
                        .stream()
                        .anyMatch(value ->
                                value != null &&
                                        value.toString()
                                                .equals(
                                                        expectedValue
                                                )
                        );
            }

            return false;
        }


        // =================================================
        // NORMAL VALUE
        // =================================================

        String actualValue =
                sourceValue.toString();


        if ("EQUALS".equalsIgnoreCase(
                operator
        )) {

            return actualValue.equals(
                    expectedValue
            );
        }

        return false;
    }


    // =====================================================
    // FIELD VALUE VALIDATION
    // =====================================================

    private void validateFieldValue(
            FormField field,
            Object value
    ) {

        String fieldType =
                field.getFieldType();


        if (fieldType == null) {
            return;
        }


        switch (fieldType.toUpperCase()) {


            case "NUMBER":

                validateNumber(
                        field,
                        value.toString()
                                .trim()
                );

                break;


            case "EMAIL":

                validateEmail(
                        field,
                        value.toString()
                                .trim()
                );

                break;


            case "SELECT":

                validateSelect(
                        field,
                        value.toString()
                                .trim()
                );

                break;


            case "MULTI_SELECT":

                validateMultiSelect(
                        field,
                        value
                );

                break;


            case "BOOLEAN":

                if (!(value instanceof Boolean) &&
                        !"true".equalsIgnoreCase(
                                value.toString()
                        ) &&
                        !"false".equalsIgnoreCase(
                                value.toString()
                        )) {

                    throw new IllegalArgumentException(
                            "Invalid boolean for field: "
                                    + field.getFieldKey()
                    );
                }

                break;


            case "DATE":

                try {

                    LocalDate.parse(
                            value.toString()
                    );

                } catch (Exception ex) {

                    throw new IllegalArgumentException(
                            "Invalid date for field: "
                                    + field.getFieldKey()
                    );
                }

                break;


            case "DATETIME":

                try {

                    LocalDateTime.parse(
                            value.toString()
                    );

                } catch (Exception ex) {

                    throw new IllegalArgumentException(
                            "Invalid datetime for field: "
                                    + field.getFieldKey()
                    );
                }

                break;


            default:

                break;
        }
    }


    // =====================================================
    // NUMBER VALIDATION
    // =====================================================

    private void validateNumber(
            FormField field,
            String value
    ) {

        try {

            new BigDecimal(
                    value
            );


            if ("phone".equalsIgnoreCase(
                    field.getFieldKey()
            )) {

                String digits =
                        value.replaceFirst(
                                "^\\+91",
                                ""
                        );


                if (!digits.matches(
                        "\\d{10}"
                )) {

                    throw new IllegalArgumentException(
                            "Phone must contain exactly 10 digits"
                    );
                }
            }

        } catch (NumberFormatException ex) {

            throw new IllegalArgumentException(
                    "Invalid number for field: "
                            + field.getFieldKey()
            );
        }
    }


    // =====================================================
    // EMAIL VALIDATION
    // =====================================================

    private void validateEmail(
            FormField field,
            String value
    ) {

        if (!value.matches(
                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
        )) {

            throw new IllegalArgumentException(
                    "Invalid email for field: "
                            + field.getFieldKey()
            );
        }
    }


    // =====================================================
    // SELECT VALIDATION
    // =====================================================

    private void validateSelect(
            FormField field,
            String value
    ) {

        boolean validOption =
                field.getOptions()
                        .stream()
                        .filter(option ->
                                Boolean.TRUE.equals(
                                        option.getActive()
                                )
                        )
                        .anyMatch(option ->
                                value.equals(
                                        option.getValue()
                                )
                        );


        if (!validOption) {

            throw new IllegalArgumentException(
                    "Invalid option for field: "
                            + field.getFieldKey()
            );
        }
    }


    // =====================================================
    // MULTI SELECT VALIDATION
    // =====================================================

    private void validateMultiSelect(
            FormField field,
            Object value
    ) {

        if (!(value instanceof List<?> selectedValues)) {

            throw new IllegalArgumentException(
                    "Invalid multi-select value for field: "
                            + field.getFieldKey()
            );
        }


        List<String> activeOptions =
                field.getOptions()
                        .stream()
                        .filter(option ->
                                Boolean.TRUE.equals(
                                        option.getActive()
                                )
                        )
                        .map(
                                FieldOption::getValue
                        )
                        .toList();


        for (Object selectedValue :
                selectedValues) {

            if (selectedValue == null) {

                throw new IllegalArgumentException(
                        "Invalid option for field: "
                                + field.getFieldKey()
                );
            }


            String selected =
                    selectedValue.toString();


            if (!activeOptions.contains(
                    selected
            )) {

                throw new IllegalArgumentException(
                        "Invalid option for field: "
                                + field.getFieldKey()
                );
            }
        }
    }


    // =====================================================
    // SET VALUE
    // =====================================================

    private void setValue(
            SubmissionValue submissionValue,
            String fieldType,
            Object value
    ) {

        // =================================================
        // MULTI SELECT
        // =================================================

        if ("MULTI_SELECT".equalsIgnoreCase(
                fieldType
        )) {

            submissionValue.setValueString(
                    value.toString()
            );

            return;
        }


        String stringValue =
                value.toString();


        if (fieldType == null) {

            submissionValue.setValueString(
                    stringValue
            );

            return;
        }


        switch (fieldType.toUpperCase()) {


            case "NUMBER":

                submissionValue.setValueNumber(
                        new BigDecimal(
                                stringValue
                        )
                );

                break;


            case "BOOLEAN":

                submissionValue.setValueBoolean(
                        Boolean.parseBoolean(
                                stringValue
                        )
                );

                break;


            case "DATE":

                submissionValue.setValueDate(
                        LocalDate.parse(
                                stringValue
                        )
                );

                break;


            case "DATETIME":

                submissionValue.setValueDatetime(
                        LocalDateTime.parse(
                                stringValue
                        )
                );

                break;


            default:

                submissionValue.setValueString(
                        stringValue
                );

                break;
        }
    }
}