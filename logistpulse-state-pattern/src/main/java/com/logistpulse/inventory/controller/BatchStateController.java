package com.logistpulse.inventory.controller;

import com.logistpulse.inventory.service.BatchLifecycleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/batches")
public class BatchStateController {

    private final BatchLifecycleService lifecycleService;

    public BatchStateController(BatchLifecycleService lifecycleService) {
        this.lifecycleService = lifecycleService;
    }

    @PostMapping("/{batchId}/{action}")
    public ResponseEntity<String> transition(@PathVariable String batchId, @PathVariable String action) {
        try {
            String newState = lifecycleService.applyTransition(batchId, action);
            return ResponseEntity.ok("Lote " + batchId + " ahora está en estado: " + newState);
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
