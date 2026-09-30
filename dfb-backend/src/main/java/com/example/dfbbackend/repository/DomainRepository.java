package com.example.dfbbackend.repository;

import com.example.dfbbackend.entity.Domain;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DomainRepository extends JpaRepository<Domain, Long> {

    Optional<Domain> findByCode(String code);

    boolean existsByCode(String code);

    Optional<Domain> findByIdAndActiveTrue(Long id);
}