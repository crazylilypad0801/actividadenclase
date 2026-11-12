package com.logistpulse.inventory.strategy.picking;

import com.logistpulse.inventory.model.Batch;
import org.springframework.stereotype.Component;
import java.util.Comparator;
import java.util.List;

@Component("FIFO")
public class FifoPickingPolicy implements PickingPolicy {
    @Override
    public Batch selectBatch(List<Batch> availableBatches) {
        return availableBatches.stream()
                .min(Comparator.comparing(Batch::getReceivedAt))
                .orElseThrow(() -> new IllegalStateException("No hay lotes disponibles"));
    }

    @Override
    public String getPolicyName() {
        return "FIFO";
    }
}
