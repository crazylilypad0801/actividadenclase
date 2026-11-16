package com.logistpulse.inventory.state;

import com.logistpulse.inventory.model.BatchContext;

public interface BatchState {
    void store(BatchContext context);
    void reserve(BatchContext context);
    void ship(BatchContext context);
    void quarantine(BatchContext context);
    String getStateName();
}
