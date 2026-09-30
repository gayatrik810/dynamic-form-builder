package com.example.dfbbackend.repository;

import com.example.dfbbackend.entity.Form;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FormRepository extends JpaRepository<Form, Long> {

    Optional<Form> findByCode(String code);
}