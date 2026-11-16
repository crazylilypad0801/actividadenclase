package com.logistpulse.inventory.state;

import com.logistpulse.inventory.model.BatchContext;

public class QuarantineState implements BatchState {
    @Override
    public BatchState store(BatchContext context) {
        System.out.println("[STATE] Lote " + context.getBatchId() + ": Cuarentena -> Almacenado (liberado)");
        return new StoredState();
    }

    @Override
    public BatchState reserve(BatchContext context) {
        throw new IllegalStateException("Transición inválida: Un lote en CUARENTENA no puede reservarse.");
    }

    @Override
    public BatchState ship(BatchContext context) {
        throw new IllegalStateException("Transición inválida: Un lote en CUARENTENA no puede enviarse.");
    }

    @Override
    public BatchState quarantine(BatchContext context) {
        throw new IllegalStateException("Transición inválida: El lote ya está en CUARENTENA.");
    }

    @Override
    public String getStateName() {
        return "CUARENTENA";
    }
}
