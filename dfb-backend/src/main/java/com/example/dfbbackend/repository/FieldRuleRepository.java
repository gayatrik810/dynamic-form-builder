package com.example.dfbbackend.repository;

import com.example.dfbbackend.entity.FieldRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FieldRuleRepository
        extends JpaRepository<FieldRule, Long> {

    List<FieldRule> findByVisitType_Id(
            Long visitTypeId
    );

    List<FieldRule> findByForm_IdAndVisitType_Id(
            Long formId,
            Long visitTypeId
    );

    void deleteBySourceField_Id(
            Long fieldId
    );

    void deleteByTargetField_Id(
            Long fieldId
    );
}