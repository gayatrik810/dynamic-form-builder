package com.example.dfbbackend.service;

import com.example.dfbbackend.dto.CreateDomainRequest;
import com.example.dfbbackend.entity.Domain;
import com.example.dfbbackend.repository.DomainRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class DomainService {

    private final DomainRepository domainRepository;

    public DomainService(
            DomainRepository domainRepository) {

        this.domainRepository = domainRepository;
    }


    // =============================================================
    // CREATE DOMAIN
    // =============================================================

    public Domain createDomain(
            CreateDomainRequest request) {

        validateRequest(request);

        String code =
                request.getCode()
                        .trim()
                        .toUpperCase();

        if (domainRepository.existsByCode(code)) {

            throw new RuntimeException(
                    "Domain code already exists: " + code
            );
        }

        Domain domain =
                new Domain();

        domain.setName(
                request.getName().trim()
        );

        domain.setCode(code);

        domain.setDescription(
                request.getDescription()
        );

        domain.setActive(true);

        LocalDateTime now =
                LocalDateTime.now();

        domain.setCreatedAt(now);

        domain.setUpdatedAt(now);

        return domainRepository.save(domain);
    }


    // =============================================================
    // GET ALL DOMAINS
    // =============================================================

    @Transactional(readOnly = true)
    public List<Domain> getAllDomains() {

        return domainRepository.findAll();
    }


    // =============================================================
    // GET DOMAIN BY ID
    // =============================================================

    @Transactional(readOnly = true)
    public Domain getDomainById(
            Long id) {

        return domainRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Domain not found with id: " + id
                        )
                );
    }


    // =============================================================
    // GET ACTIVE DOMAIN BY ID
    //
    // Useful for user-side domain access validation.
    // =============================================================

    @Transactional(readOnly = true)
    public Domain getActiveDomainById(
            Long id) {

        return domainRepository
                .findByIdAndActiveTrue(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Domain is inactive or not found"
                        )
                );
    }


    // =============================================================
    // GET DOMAIN BY CODE
    // =============================================================

    @Transactional(readOnly = true)
    public Domain getDomainByCode(
            String code) {

        return domainRepository
                .findByCode(
                        code.trim().toUpperCase()
                )
                .orElseThrow(() ->
                        new RuntimeException(
                                "Domain not found with code: "
                                        + code
                        )
                );
    }


    // =============================================================
    // UPDATE DOMAIN
    // =============================================================

    public Domain updateDomain(
            Long id,
            CreateDomainRequest request) {

        validateRequest(request);

        Domain domain =
                getDomainById(id);

        String newCode =
                request.getCode()
                        .trim()
                        .toUpperCase();

        // ---------------------------------------------------------
        // Check duplicate code only if code changed
        // ---------------------------------------------------------

        if (
                !domain.getCode()
                        .equalsIgnoreCase(newCode)

                        && domainRepository
                        .existsByCode(newCode)
        ) {

            throw new RuntimeException(
                    "Domain code already exists: "
                            + newCode
            );
        }


        domain.setName(
                request.getName().trim()
        );

        domain.setCode(
                newCode
        );

        domain.setDescription(
                request.getDescription()
        );

        domain.setUpdatedAt(
                LocalDateTime.now()
        );

        return domainRepository.save(domain);
    }


    // =============================================================
    // DEACTIVATE DOMAIN
    // =============================================================

    public Domain deactivateDomain(
            Long id) {

        Domain domain =
                getDomainById(id);

        if (!Boolean.TRUE.equals(
                domain.getActive()
        )) {

            return domain;
        }

        domain.setActive(false);

        domain.setUpdatedAt(
                LocalDateTime.now()
        );

        return domainRepository.save(domain);
    }


    // =============================================================
    // ACTIVATE DOMAIN
    // =============================================================

    public Domain activateDomain(
            Long id) {

        Domain domain =
                getDomainById(id);

        if (Boolean.TRUE.equals(
                domain.getActive()
        )) {

            return domain;
        }

        domain.setActive(true);

        domain.setUpdatedAt(
                LocalDateTime.now()
        );

        return domainRepository.save(domain);
    }


    // =============================================================
    // REQUEST VALIDATION
    // =============================================================

    private void validateRequest(
            CreateDomainRequest request) {

        if (request == null) {

            throw new RuntimeException(
                    "Domain request cannot be empty"
            );
        }


        if (
                request.getName() == null
                        || request.getName()
                        .trim()
                        .isEmpty()
        ) {

            throw new RuntimeException(
                    "Domain name is required"
            );
        }


        if (
                request.getCode() == null
                        || request.getCode()
                        .trim()
                        .isEmpty()
        ) {

            throw new RuntimeException(
                    "Domain code is required"
            );
        }
    }
}