package com.logistpulse.inventory.controller;

import com.logistpulse.inventory.model.Batch;
import com.logistpulse.inventory.service.InventoryStrategyContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/strategies")
public class StrategyController {

    private final InventoryStrategyContext strategyContext;

    public StrategyController(InventoryStrategyContext strategyContext) {
        this.strategyContext = strategyContext;
    }

    @GetMapping("/picking/{policy}")
    public ResponseEntity<String> pickBatch(@PathVariable String policy) {
        Batch selected = strategyContext.executePicking(policy, strategyContext.getSampleBatches());
        return ResponseEntity.ok("Política [" + policy.toUpperCase() + "] eligió el lote: " + selected.getBatchId());
    }

    @GetMapping("/replenishment/{policy}")
    public ResponseEntity<String> calculateReplenishment(
            @PathVariable String policy,
            @RequestParam int currentStock,
            @RequestParam int dailyDemand) {
        int qty = strategyContext.executeReplenishment(policy, currentStock, dailyDemand);
        return ResponseEntity.ok("Política [" + policy.toUpperCase() + "] sugiere reponer: " + qty + " unidades.");
    }
}
