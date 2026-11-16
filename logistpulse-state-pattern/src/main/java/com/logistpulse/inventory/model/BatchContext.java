package com.logistpulse.inventory.model;

import com.logistpulse.inventory.state.BatchState;
import com.logistpulse.inventory.state.ReceivedState;

public class BatchContext {
    private final String batchId;
    private BatchState currentState;

    public BatchContext(String batchId) {
        this.batchId = batchId;
        this.currentState = new ReceivedState();
    }

    public void store() {
        currentState.store(this);
    }

    public void reserve() {
        currentState.reserve(this);
    }

    public void ship() {
        currentState.ship(this);
    }

    public void quarantine() {
        currentState.quarantine(this);
    }

    public String getBatchId() {
        return batchId;
    }

    public String getCurrentStateName() {
        return currentState.getStateName();
    }

    public void setState(BatchState state) {
        this.currentState = state;
    }
}
