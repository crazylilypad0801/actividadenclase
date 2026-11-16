package com.logistpulse.inventory.state;

import com.logistpulse.inventory.model.BatchContext;

public interface BatchState {
    BatchState store(BatchContext context);
    BatchState reserve(BatchContext context);
    BatchState ship(BatchContext context);
    BatchState quarantine(BatchContext context);
    String getStateName();
}
