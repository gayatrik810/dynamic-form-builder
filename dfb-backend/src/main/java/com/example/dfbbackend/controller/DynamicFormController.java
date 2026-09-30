package com.example.dfbbackend.controller;

import com.example.dfbbackend.dto.DynamicFieldResponse;
import com.example.dfbbackend.service.DynamicFormService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/form-fields")
public class DynamicFormController {

    private final DynamicFormService dynamicFormService;

    public DynamicFormController(DynamicFormService dynamicFormService) {
        this.dynamicFormService = dynamicFormService;
    }

    @GetMapping("/visit-type/{visitTypeId}")
    public List<DynamicFieldResponse> getFieldsByVisitType(
            @PathVariable Long visitTypeId
    ) {
        return dynamicFormService.getFieldsByVisitType(visitTypeId);
    }
}