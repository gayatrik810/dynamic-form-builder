package com.example.dfbbackend.repository;

import com.example.dfbbackend.entity.FieldOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FieldOptionRepository
        extends JpaRepository<FieldOption, Long> {

    void deleteByField_Id(Long fieldId);
}