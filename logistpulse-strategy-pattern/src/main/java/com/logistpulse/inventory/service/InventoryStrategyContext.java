package com.logistpulse.inventory.service;

import com.logistpulse.inventory.model.Batch;
import com.logistpulse.inventory.strategy.picking.PickingPolicy;
import com.logistpulse.inventory.strategy.replenishment.ReplenishmentPolicy;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

@Service
public class InventoryStrategyContext {

    private final Map<String, PickingPolicy> pickingPolicies;
    private final Map<String, ReplenishmentPolicy> replenishmentPolicies;

    public InventoryStrategyContext(
            Map<String, PickingPolicy> pickingPolicies,
            Map<String, ReplenishmentPolicy> replenishmentPolicies) {
        this.pickingPolicies = pickingPolicies;
        this.replenishmentPolicies = replenishmentPolicies;
    }

    public Batch executePicking(String policyKey, List<Batch> batches) {
        if (batches == null || batches.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("Available batches must not be null or contain null entries.");
        }
        PickingPolicy policy = getPolicy(policyKey, pickingPolicies, "picking");
        return policy.selectBatch(batches);
    }

    public int executeReplenishment(String policyKey, int currentStock, int dailyDemand) {
        if (currentStock < 0 || dailyDemand < 0) {
            throw new IllegalArgumentException("Stock and daily demand must not be negative.");
        }
        ReplenishmentPolicy policy = getPolicy(policyKey, replenishmentPolicies, "replenishment");
        return policy.calculateOrderQuantity(currentStock, dailyDemand);
    }

    private <T> T getPolicy(String policyKey, Map<String, T> policies, String category) {
        if (policyKey == null || policyKey.isBlank()) {
            throw new IllegalArgumentException("A " + category + " policy must be specified.");
        }
        T policy = policies.get(policyKey.trim().toUpperCase(Locale.ROOT));
        if (policy == null) {
            throw new IllegalArgumentException("Unknown " + category + " policy: " + policyKey);
        }
        return policy;
    }

    public List<Batch> getSampleBatches() {
        return List.of(
            new Batch("LOTE-A", LocalDate.now().minusDays(10), LocalDate.now().plusDays(15)),
            new Batch("LOTE-B", LocalDate.now().minusDays(5), LocalDate.now().plusDays(5)),
            new Batch("LOTE-C", LocalDate.now().minusDays(1), LocalDate.now().plusDays(60))
        );
    }
}
