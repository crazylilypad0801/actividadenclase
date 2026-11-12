package com.logistpulse.inventory.strategy.picking;

import com.logistpulse.inventory.model.Batch;
import java.util.List;

public interface PickingPolicy {
    Batch selectBatch(List<Batch> availableBatches);
    String getPolicyName();
}
