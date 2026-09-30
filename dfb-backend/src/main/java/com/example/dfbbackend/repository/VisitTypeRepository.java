package com.example.dfbbackend.repository;

import com.example.dfbbackend.entity.VisitType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VisitTypeRepository
        extends JpaRepository<VisitType, Long> {

    List<VisitType> findByDomainIdAndActiveTrue(
            Long domainId
    );

    List<VisitType> findByDomainId(
            Long domainId
    );

    boolean existsByDomainIdAndName(
            Long domainId,
            String name
    );

    boolean existsByDomainIdAndNameAndIdNot(
            Long domainId,
            String name,
            Long id
    );
}