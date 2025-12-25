package com.example.demo.controller;

import com.example.demo.model.DeliveryEvaluation;
import com.example.demo.service.DeliveryEvaluationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/evaluations")
public class DeliveryEvaluationController {

    private final DeliveryEvaluationService service;

    public DeliveryEvaluationController(DeliveryEvaluationService service) {
        this.service = service;
    }

    @PostMapping
    public DeliveryEvaluation create(@RequestBody DeliveryEvaluation evaluation) {
        return service.createEvaluation(evaluation);
    }

    @GetMapping("/{id}")
    public DeliveryEvaluation get(@PathVariable Long id) {
        return service.getEvaluationById(id);
    }

    @GetMapping("/vendor/{vendorId}")
    public List<DeliveryEvaluation> byVendor(@PathVariable Long vendorId) {
        return service.getEvaluationsForVendor(vendorId);
    }

    @GetMapping("/requirement/{reqId}")
    public List<DeliveryEvaluation> byRequirement(@PathVariable Long reqId) {
        return service.getEvaluationsForRequirement(reqId);
    }
}
