package com.logistpulse.inventory.service;

import com.logistpulse.inventory.model.BatchContext;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class BatchLifecycleService {
    private final Map<String, BatchContext> batches = new ConcurrentHashMap<>();

    public BatchContext getOrCreateBatch(String batchId) {
        return batches.computeIfAbsent(batchId, BatchContext::new);
    }

    public String applyTransition(String batchId, String action) {
        if (action == null || action.isBlank()) {
            throw new IllegalArgumentException("Action must not be blank.");
        }
        BatchContext batch = getOrCreateBatch(batchId);
        switch (action.toLowerCase(Locale.ROOT)) {
            case "store" -> batch.store();
            case "reserve" -> batch.reserve();
            case "ship" -> batch.ship();
            case "quarantine" -> batch.quarantine();
            default -> throw new IllegalArgumentException("Acción desconocida: " + action);
        }
        return batch.getCurrentStateName();
    }
}
