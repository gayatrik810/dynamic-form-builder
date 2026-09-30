package com.example.dfbbackend.controller;

import com.example.dfbbackend.dto.CreateDomainRequest;
import com.example.dfbbackend.dto.DomainResponse;
import com.example.dfbbackend.entity.Domain;
import com.example.dfbbackend.service.DomainService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/domains")
public class DomainController {

    private final DomainService domainService;

    public DomainController(DomainService domainService) {
        this.domainService = domainService;
    }


    // =============================================================
    // GET ALL DOMAINS
    //
    // Kept available for authenticated users because the
    // User Registration page needs the domain list.
    // =============================================================

    @GetMapping
    public List<DomainResponse> getAllDomains() {

        return domainService.getAllDomains()
                .stream()
                .map(this::toResponse)
                .toList();
    }


    // =============================================================
    // GET DOMAIN BY ID
    // =============================================================

    @GetMapping("/{id}")
    public DomainResponse getDomainById(
            @PathVariable Long id) {

        return toResponse(
                domainService.getDomainById(id)
        );
    }


    // =============================================================
    // CREATE DOMAIN
    //
    // ADMIN ONLY
    // =============================================================

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DomainResponse createDomain(
            @RequestBody CreateDomainRequest request) {

        Domain domain =
                domainService.createDomain(request);

        return toResponse(domain);
    }


    // =============================================================
    // UPDATE DOMAIN
    //
    // ADMIN ONLY
    // =============================================================

    @PutMapping("/{id}")
    public DomainResponse updateDomain(
            @PathVariable Long id,
            @RequestBody CreateDomainRequest request) {

        return toResponse(
                domainService.updateDomain(
                        id,
                        request
                )
        );
    }


    // =============================================================
    // DEACTIVATE DOMAIN
    //
    // ADMIN ONLY
    // =============================================================

    @PatchMapping("/{id}/deactivate")
    public DomainResponse deactivateDomain(
            @PathVariable Long id) {

        return toResponse(
                domainService.deactivateDomain(id)
        );
    }


    // =============================================================
    // ACTIVATE DOMAIN
    //
    // ADMIN ONLY
    // =============================================================

    @PatchMapping("/{id}/activate")
    public DomainResponse activateDomain(
            @PathVariable Long id) {

        return toResponse(
                domainService.activateDomain(id)
        );
    }


    // =============================================================
    // RESPONSE MAPPER
    // =============================================================

    private DomainResponse toResponse(
            Domain domain) {

        return new DomainResponse(
                domain.getId(),
                domain.getName(),
                domain.getCode(),
                domain.getDescription(),
                domain.getActive()
        );
    }
}