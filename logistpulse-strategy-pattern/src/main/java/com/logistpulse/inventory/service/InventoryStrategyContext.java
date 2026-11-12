package com.logistpulse.inventory.service;

import com.logistpulse.inventory.model.Batch;
import com.logistpulse.inventory.strategy.picking.PickingPolicy;
import com.logistpulse.inventory.strategy.replenishment.ReplenishmentPolicy;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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
        PickingPolicy policy = pickingPolicies.getOrDefault(policyKey.toUpperCase(), pickingPolicies.get("FIFO"));
        return policy.selectBatch(batches);
    }

    public int executeReplenishment(String policyKey, int currentStock, int dailyDemand) {
        ReplenishmentPolicy policy = replenishmentPolicies.getOrDefault(policyKey.toUpperCase(), replenishmentPolicies.get("MIN_MAX"));
        return policy.calculateOrderQuantity(currentStock, dailyDemand);
    }

    public List<Batch> getSampleBatches() {
        return List.of(
            new Batch("LOTE-A", LocalDate.now().minusDays(10), LocalDate.now().plusDays(15)),
            new Batch("LOTE-B", LocalDate.now().minusDays(5), LocalDate.now().plusDays(5)),
            new Batch("LOTE-C", LocalDate.now().minusDays(1), LocalDate.now().plusDays(60))
        );
    }
}
