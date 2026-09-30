package com.example.dfbbackend.controller;

import com.example.dfbbackend.dto.CreateVisitTypeRequest;
import com.example.dfbbackend.dto.VisitTypeDeleteInfoResponse;
import com.example.dfbbackend.dto.VisitTypeResponse;
import com.example.dfbbackend.entity.VisitType;
import com.example.dfbbackend.service.VisitTypeService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api")
public class VisitTypeController {


    private final VisitTypeService visitTypeService;


    public VisitTypeController(
            VisitTypeService visitTypeService
    ) {

        this.visitTypeService =
                visitTypeService;
    }


    // =====================================================
    // CREATE
    // =====================================================

    @PostMapping(
            "/domains/{domainId}/visit-types"
    )
    @ResponseStatus(HttpStatus.CREATED)
    public VisitTypeResponse createVisitType(
            @PathVariable Long domainId,
            @RequestBody CreateVisitTypeRequest request
    ) {

        return toResponse(
                visitTypeService.createVisitType(
                        domainId,
                        request.getName()
                )
        );
    }


    // =====================================================
    // GET BY DOMAIN
    // =====================================================

    @GetMapping(
            "/domains/{domainId}/visit-types"
    )
    public List<VisitTypeResponse> getVisitTypes(
            @PathVariable Long domainId
    ) {

        return visitTypeService
                .getVisitTypesByDomain(domainId)
                .stream()
                .map(this::toResponse)
                .toList();
    }


    // =====================================================
    // GET ONE
    // =====================================================

    @GetMapping(
            "/visit-types/{id}"
    )
    public VisitTypeResponse getVisitTypeById(
            @PathVariable Long id
    ) {

        return toResponse(
                visitTypeService
                        .getVisitTypeById(id)
        );
    }


    // =====================================================
    // GET DELETE INFORMATION
    // =====================================================

    @GetMapping(
            "/visit-types/{id}/delete-info"
    )
    public VisitTypeDeleteInfoResponse getDeleteInfo(
            @PathVariable Long id
    ) {

        return visitTypeService.getDeleteInfo(id);
    }


    // =====================================================
    // UPDATE
    // =====================================================

    @PutMapping(
            "/visit-types/{id}"
    )
    public VisitTypeResponse updateVisitType(
            @PathVariable Long id,
            @RequestBody CreateVisitTypeRequest request
    ) {

        return toResponse(
                visitTypeService.updateVisitType(
                        id,
                        request.getName()
                )
        );
    }


    // =====================================================
    // DEACTIVATE
    // =====================================================

    @PatchMapping(
            "/visit-types/{id}/deactivate"
    )
    public VisitTypeResponse deactivateVisitType(
            @PathVariable Long id
    ) {

        return toResponse(
                visitTypeService
                        .deactivateVisitType(id)
        );
    }


    // =====================================================
    // ACTIVATE
    // =====================================================

    @PatchMapping(
            "/visit-types/{id}/activate"
    )
    public VisitTypeResponse activateVisitType(
            @PathVariable Long id
    ) {

        return toResponse(
                visitTypeService
                        .activateVisitType(id)
        );
    }


    // =====================================================
    // PERMANENT DELETE
    // =====================================================

    @DeleteMapping(
            "/visit-types/{id}"
    )
    public ResponseEntity<Map<String, Object>> deleteVisitType(
            @PathVariable Long id
    ) {

        long deletedSubmissionCount =
                visitTypeService.deleteVisitType(id);


        Map<String, Object> response =
                new HashMap<>();


        response.put(
                "message",
                "Visit type permanently deleted"
        );

        response.put(
                "visitTypeId",
                id
        );

        response.put(
                "deletedSubmissions",
                deletedSubmissionCount
        );


        return ResponseEntity.ok(response);
    }


    // =====================================================
    // RESPONSE
    // =====================================================

    private VisitTypeResponse toResponse(
            VisitType visitType
    ) {

        return new VisitTypeResponse(
                visitType.getId(),
                visitType.getDomain().getId(),
                visitType.getDomain().getName(),
                visitType.getName(),
                visitType.getActive()
        );
    }
}