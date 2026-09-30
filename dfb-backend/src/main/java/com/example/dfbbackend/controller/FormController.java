package com.example.dfbbackend.controller;

import com.example.dfbbackend.dto.CreateFormRequest;
import com.example.dfbbackend.dto.FormResponse;
import com.example.dfbbackend.entity.Form;
import com.example.dfbbackend.entity.FormVisitType;
import com.example.dfbbackend.service.FormService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/forms")
public class FormController {

    private final FormService formService;

    public FormController(FormService formService) {
        this.formService = formService;
    }

    // =========================================================
    // CREATE FORM
    // =========================================================

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FormResponse createForm(
            @RequestBody CreateFormRequest request
    ) {

        Form form = formService.createForm(request);

        return toResponse(form);
    }

    // =========================================================
    // GET ALL FORMS
    // =========================================================

    @GetMapping
    public List<FormResponse> getAllForms() {

        return formService.getAllForms()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // =========================================================
    // GET FORM BY ID
    // =========================================================

    @GetMapping("/{id}")
    public FormResponse getFormById(
            @PathVariable Long id
    ) {

        return toResponse(
                formService.getFormById(id)
        );
    }

    // =========================================================
    // UPDATE FORM
    // =========================================================

    @PutMapping("/{id}")
    public FormResponse updateForm(
            @PathVariable Long id,
            @RequestBody CreateFormRequest request
    ) {

        Form form =
                formService.updateForm(id, request);

        return toResponse(form);
    }

    // =========================================================
    // DEACTIVATE FORM
    // =========================================================

    @PatchMapping("/{id}/deactivate")
    public FormResponse deactivateForm(
            @PathVariable Long id
    ) {

        return toResponse(
                formService.deactivateForm(id)
        );
    }

    // =========================================================
    // ACTIVATE FORM
    // =========================================================

    @PatchMapping("/{id}/activate")
    public FormResponse activateForm(
            @PathVariable Long id
    ) {

        return toResponse(
                formService.activateForm(id)
        );
    }

    // =========================================================
    // DELETE FORM
    // =========================================================

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteForm(
            @PathVariable Long id
    ) {

        formService.deleteForm(id);
    }

    // =========================================================
    // RESPONSE MAPPING
    // =========================================================

    private FormResponse toResponse(Form form) {

        List<FormVisitType> mappings =
                formService.getFormVisitTypes(form.getId());

        List<Long> visitTypeIds =
                mappings.stream()
                        .map(mapping ->
                                mapping.getVisitType().getId()
                        )
                        .toList();

        List<String> visitTypeNames =
                mappings.stream()
                        .map(mapping ->
                                mapping.getVisitType().getName()
                        )
                        .toList();

        return new FormResponse(
                form.getId(),
                form.getDomain().getId(),
                form.getDomain().getName(),
                form.getName(),
                form.getCode(),
                form.getDescription(),
                form.getActive(),
                visitTypeIds,
                visitTypeNames
        );
    }
}