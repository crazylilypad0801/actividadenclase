package com.logistpulse.inventory.strategy.picking;

import com.logistpulse.inventory.model.Batch;
import org.springframework.stereotype.Component;
import java.util.Comparator;
import java.util.List;

@Component("FEFO")
public class FefoPickingPolicy implements PickingPolicy {
    @Override
    public Batch selectBatch(List<Batch> availableBatches) {
        return availableBatches.stream()
                .min(Comparator.comparing(Batch::getExpirationDate))
                .orElseThrow(() -> new IllegalStateException("No hay lotes disponibles"));
    }

    @Override
    public String getPolicyName() {
        return "FEFO";
    }
}
