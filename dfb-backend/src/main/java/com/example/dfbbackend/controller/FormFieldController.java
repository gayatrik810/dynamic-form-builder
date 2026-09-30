package com.example.dfbbackend.controller;

import com.example.dfbbackend.dto.CreateFormFieldRequest;
import com.example.dfbbackend.dto.FormFieldResponse;
import com.example.dfbbackend.service.FormFieldService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class FormFieldController {

    private final FormFieldService formFieldService;


    public FormFieldController(
            FormFieldService formFieldService
    ) {

        this.formFieldService =
                formFieldService;
    }


    // =========================================================
    // GET ALL FIELDS FOR FORM
    // =========================================================

    @GetMapping("/forms/{formId}/fields")
    public ResponseEntity<List<FormFieldResponse>> getFieldsByForm(
            @PathVariable Long formId
    ) {

        return ResponseEntity.ok(
                formFieldService.getFieldsByForm(formId)
        );
    }


    // =========================================================
    // CREATE FIELD
    // =========================================================

    @PostMapping("/forms/{formId}/fields")
    public ResponseEntity<FormFieldResponse> createField(
            @PathVariable Long formId,
            @RequestBody CreateFormFieldRequest request
    ) {

        return ResponseEntity.ok(
                formFieldService.createField(
                        formId,
                        request
                )
        );
    }


    // =========================================================
    // GET SINGLE FIELD
    // =========================================================

    @GetMapping("/form-fields/{fieldId}")
    public ResponseEntity<FormFieldResponse> getFieldById(
            @PathVariable Long fieldId
    ) {

        return ResponseEntity.ok(
                formFieldService.getFieldById(fieldId)
        );
    }


    // =========================================================
    // UPDATE FIELD
    // =========================================================

    @PutMapping("/form-fields/{fieldId}")
    public ResponseEntity<FormFieldResponse> updateField(
            @PathVariable Long fieldId,
            @RequestBody CreateFormFieldRequest request
    ) {

        return ResponseEntity.ok(
                formFieldService.updateField(
                        fieldId,
                        request
                )
        );
    }


    // =========================================================
    // DEACTIVATE FIELD
    // =========================================================

    @PatchMapping("/form-fields/{fieldId}/deactivate")
    public ResponseEntity<FormFieldResponse> deactivateField(
            @PathVariable Long fieldId
    ) {

        return ResponseEntity.ok(
                formFieldService.deactivateField(
                        fieldId
                )
        );
    }


    // =========================================================
    // ACTIVATE FIELD
    // =========================================================

    @PatchMapping("/form-fields/{fieldId}/activate")
    public ResponseEntity<FormFieldResponse> activateField(
            @PathVariable Long fieldId
    ) {

        return ResponseEntity.ok(
                formFieldService.activateField(
                        fieldId
                )
        );
    }


    // =========================================================
    // DELETE FIELD
    // =========================================================

    @DeleteMapping("/form-fields/{fieldId}")
    public ResponseEntity<Void> deleteField(
            @PathVariable Long fieldId
    ) {

        formFieldService.deleteField(
                fieldId
        );

        return ResponseEntity.noContent().build();
    }
}