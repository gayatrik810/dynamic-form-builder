package com.example.dfbbackend.repository;

import com.example.dfbbackend.entity.FormVisitType;
import com.example.dfbbackend.entity.FormVisitTypeId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FormVisitTypeRepository
        extends JpaRepository<FormVisitType, FormVisitTypeId> {

    List<FormVisitType> findByForm_Id(
            Long formId
    );

    List<FormVisitType> findByVisitType_Id(
            Long visitTypeId
    );

    void deleteByForm_Id(
            Long formId
    );

    void deleteByVisitType_Id(
            Long visitTypeId
    );
}