package com.example.dfbbackend.service;

import com.example.dfbbackend.dto.DynamicFieldResponse;
import com.example.dfbbackend.dto.FieldOptionResponse;
import com.example.dfbbackend.dto.FieldRuleResponse;
import com.example.dfbbackend.entity.FieldOption;
import com.example.dfbbackend.entity.FieldRule;
import com.example.dfbbackend.entity.FormField;
import com.example.dfbbackend.entity.FormFieldVisitType;
import com.example.dfbbackend.exception.ResourceNotFoundException;
import com.example.dfbbackend.repository.FieldRuleRepository;
import com.example.dfbbackend.repository.FormFieldVisitTypeRepository;
import com.example.dfbbackend.repository.VisitTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class DynamicFormService {

    private final VisitTypeRepository visitTypeRepository;
    private final FormFieldVisitTypeRepository mappingRepository;
    private final FieldRuleRepository fieldRuleRepository;

    public DynamicFormService(
            VisitTypeRepository visitTypeRepository,
            FormFieldVisitTypeRepository mappingRepository,
            FieldRuleRepository fieldRuleRepository
    ) {
        this.visitTypeRepository = visitTypeRepository;
        this.mappingRepository = mappingRepository;
        this.fieldRuleRepository = fieldRuleRepository;
    }

    public List<DynamicFieldResponse> getFieldsByVisitType(Long visitTypeId) {

        // Make sure the visit type exists
        visitTypeRepository.findById(visitTypeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Visit type not found with id: " + visitTypeId
                        )
                );

        // Get fields belonging to this visit type
        List<FormFieldVisitType> mappings =
                mappingRepository.findByVisitType_Id(visitTypeId);

        // Get conditional rules for this visit type
        List<FieldRule> rules =
                fieldRuleRepository.findByVisitType_Id(visitTypeId);

        /*
         * IMPORTANT:
         *
         * Rules are attached to the TARGET field.
         *
         * Example:
         *
         * Source field:
         *     Business Type
         *
         * Target field:
         *     Other Business Type
         *
         * Rule:
         *     Business Type == Other
         *     -> SHOW Other Business Type
         *
         * Therefore we group rules by TARGET field ID.
         */
        Map<Long, List<FieldRule>> rulesByTargetField =
                rules.stream()
                        .collect(Collectors.groupingBy(
                                rule -> rule.getTargetField().getId()
                        ));

        return mappings.stream()
                .map(FormFieldVisitType::getFormField)
                .filter(FormField::getActive)
                .sorted(
                        Comparator.comparing(
                                FormField::getDisplayOrder
                        )
                )
                .map(field ->
                        toResponse(
                                field,
                                rulesByTargetField.getOrDefault(
                                        field.getId(),
                                        List.of()
                                )
                        )
                )
                .toList();
    }

    private DynamicFieldResponse toResponse(
            FormField field,
            List<FieldRule> rules
    ) {

        DynamicFieldResponse response =
                new DynamicFieldResponse();

        response.setId(field.getId());
        response.setFieldKey(field.getFieldKey());
        response.setLabel(field.getLabel());
        response.setFieldType(field.getFieldType());
        response.setRequired(field.getRequired());
        response.setDisplayOrder(field.getDisplayOrder());
        response.setPlaceholder(field.getPlaceholder());
        response.setValidationRule(field.getValidationRule());
        response.setActive(field.getActive());

        // Active options only
        List<FieldOptionResponse> options =
                field.getOptions()
                        .stream()
                        .filter(FieldOption::getActive)
                        .sorted(
                                Comparator.comparing(
                                        FieldOption::getDisplayOrder
                                )
                        )
                        .map(this::toOptionResponse)
                        .toList();

        response.setOptions(options);

        // Conditional rules belonging to this TARGET field
        List<FieldRuleResponse> ruleResponses =
                rules.stream()
                        .map(this::toRuleResponse)
                        .toList();

        response.setRules(ruleResponses);

        return response;
    }

    private FieldOptionResponse toOptionResponse(
            FieldOption option
    ) {

        FieldOptionResponse response =
                new FieldOptionResponse();

        response.setId(option.getId());
        response.setLabel(option.getLabel());
        response.setValue(option.getValue());
        response.setDisplayOrder(option.getDisplayOrder());
        response.setActive(option.getActive());

        return response;
    }

    private FieldRuleResponse toRuleResponse(
            FieldRule rule
    ) {

        FieldRuleResponse response =
                new FieldRuleResponse();

        response.setId(rule.getId());

        response.setSourceFieldId(
                rule.getSourceField().getId()
        );

        response.setOperator(
                rule.getOperator()
        );

        response.setExpectedValue(
                rule.getExpectedValue()
        );

        response.setTargetFieldId(
                rule.getTargetField().getId()
        );

        response.setAction(
                rule.getAction()
        );

        return response;
    }
}