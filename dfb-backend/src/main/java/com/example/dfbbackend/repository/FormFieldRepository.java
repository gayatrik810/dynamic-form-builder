package com.example.dfbbackend.repository;

import com.example.dfbbackend.entity.FormField;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FormFieldRepository
        extends JpaRepository<FormField, Long> {

    List<FormField> findByFormIdAndActiveTrueOrderByDisplayOrderAsc(
            Long formId
    );

    List<FormField> findByFormIdOrderByDisplayOrderAsc(
            Long formId
    );

    boolean existsByFormIdAndFieldKey(
            Long formId,
            String fieldKey
    );
}