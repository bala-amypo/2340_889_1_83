package com.example.demo.controller;

import com.example.demo.model.DeliveryEvaluation;
import com.example.demo.service.DeliveryEvaluationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/evaluations")
@Tag(name = "Delivery Evaluations", description = "Delivery evaluation management")
@SecurityRequirement(name = "Bearer Authentication")
public class DeliveryEvaluationController {
    private final DeliveryEvaluationService deliveryEvaluationService;

    public DeliveryEvaluationController(DeliveryEvaluationService deliveryEvaluationService) {
        this.deliveryEvaluationService = deliveryEvaluationService;
    }

    @PostMapping
    @Operation(summary = "Create delivery evaluation")
    public ResponseEntity<DeliveryEvaluation> createEvaluation(@RequestBody DeliveryEvaluation evaluation) {
        return ResponseEntity.ok(deliveryEvaluationService.createEvaluation(evaluation));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get evaluation by ID")
    public ResponseEntity<DeliveryEvaluation> getEvaluation(@PathVariable Long id) {
        return ResponseEntity.ok(deliveryEvaluationService.getEvaluationById(id));
    }

    @GetMapping("/vendor/{vendorId}")
    @Operation(summary = "Get evaluations for vendor")
    public ResponseEntity<List<DeliveryEvaluation>> getEvaluationsForVendor(@PathVariable Long vendorId) {
        return ResponseEntity.ok(deliveryEvaluationService.getEvaluationsForVendor(vendorId));
    }

    @GetMapping("/requirement/{reqId}")
    @Operation(summary = "Get evaluations for requirement")
    public ResponseEntity<List<DeliveryEvaluation>> getEvaluationsForRequirement(@PathVariable Long reqId) {
        return ResponseEntity.ok(deliveryEvaluationService.getEvaluationsForRequirement(reqId));
    }
}