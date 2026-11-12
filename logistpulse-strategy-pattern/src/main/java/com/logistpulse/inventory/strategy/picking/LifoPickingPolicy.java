package com.logistpulse.inventory.strategy.picking;

import com.logistpulse.inventory.model.Batch;
import org.springframework.stereotype.Component;
import java.util.Comparator;
import java.util.List;

@Component("LIFO")
public class LifoPickingPolicy implements PickingPolicy {
    @Override
    public Batch selectBatch(List<Batch> availableBatches) {
        return availableBatches.stream()
                .max(Comparator.comparing(Batch::getReceivedAt))
                .orElseThrow(() -> new IllegalStateException("No hay lotes disponibles"));
    }

    @Override
    public String getPolicyName() {
        return "LIFO";
    }
}
