package com.example.dfbbackend.repository;

import com.example.dfbbackend.entity.FormFieldVisitType;
import com.example.dfbbackend.entity.FormFieldVisitTypeId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FormFieldVisitTypeRepository
        extends JpaRepository<FormFieldVisitType, FormFieldVisitTypeId> {

    List<FormFieldVisitType> findByVisitType_Id(
            Long visitTypeId
    );

    List<FormFieldVisitType> findByFormField_Id(
            Long formFieldId
    );

    void deleteByFormField_IdAndVisitType_Id(
            Long formFieldId,
            Long visitTypeId
    );

    void deleteByFormField_Id(
            Long formFieldId
    );

    void deleteByVisitType_Id(
            Long visitTypeId
    );
}