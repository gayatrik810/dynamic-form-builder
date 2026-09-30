package com.example.dfbbackend.service;

import com.example.dfbbackend.entity.FormField;
import com.example.dfbbackend.entity.FormFieldVisitType;
import com.example.dfbbackend.entity.FormFieldVisitTypeId;
import com.example.dfbbackend.entity.VisitType;
import com.example.dfbbackend.exception.ResourceNotFoundException;
import com.example.dfbbackend.repository.FormFieldRepository;
import com.example.dfbbackend.repository.FormFieldVisitTypeRepository;
import com.example.dfbbackend.repository.VisitTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class FormFieldVisitTypeService {

    private final FormFieldVisitTypeRepository mappingRepository;
    private final FormFieldRepository formFieldRepository;
    private final VisitTypeRepository visitTypeRepository;

    public FormFieldVisitTypeService(
            FormFieldVisitTypeRepository mappingRepository,
            FormFieldRepository formFieldRepository,
            VisitTypeRepository visitTypeRepository
    ) {
        this.mappingRepository = mappingRepository;
        this.formFieldRepository = formFieldRepository;
        this.visitTypeRepository = visitTypeRepository;
    }

    public FormFieldVisitType createMapping(
            Long formFieldId,
            Long visitTypeId
    ) {
        FormField formField = formFieldRepository.findById(formFieldId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Form field not found with id: " + formFieldId
                        )
                );

        VisitType visitType = visitTypeRepository.findById(visitTypeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Visit type not found with id: " + visitTypeId
                        )
                );

        FormFieldVisitTypeId mappingId =
                new FormFieldVisitTypeId(formFieldId, visitTypeId);

        // Option A: duplicate mapping is an error
        if (mappingRepository.existsById(mappingId)) {
            throw new IllegalArgumentException(
                    "Form field is already mapped to this visit type"
            );
        }

        FormFieldVisitType mapping = new FormFieldVisitType();

        mapping.setId(mappingId);
        mapping.setFormField(formField);
        mapping.setVisitType(visitType);

        return mappingRepository.save(mapping);
    }

    @Transactional(readOnly = true)
    public List<FormFieldVisitType> getMappingsByVisitType(
            Long visitTypeId
    ) {
        if (!visitTypeRepository.existsById(visitTypeId)) {
            throw new ResourceNotFoundException(
                    "Visit type not found with id: " + visitTypeId
            );
        }

        return mappingRepository.findByVisitType_Id(visitTypeId);
    }

    @Transactional(readOnly = true)
    public List<FormFieldVisitType> getMappingsByFormField(
            Long formFieldId
    ) {
        if (!formFieldRepository.existsById(formFieldId)) {
            throw new ResourceNotFoundException(
                    "Form field not found with id: " + formFieldId
            );
        }

        return mappingRepository.findByFormField_Id(formFieldId);
    }

    public void deleteMapping(
            Long formFieldId,
            Long visitTypeId
    ) {
        FormFieldVisitTypeId mappingId =
                new FormFieldVisitTypeId(formFieldId, visitTypeId);

        if (!mappingRepository.existsById(mappingId)) {
            throw new ResourceNotFoundException(
                    "Mapping not found for form field id "
                            + formFieldId
                            + " and visit type id "
                            + visitTypeId
            );
        }

        mappingRepository.deleteById(mappingId);
    }
}