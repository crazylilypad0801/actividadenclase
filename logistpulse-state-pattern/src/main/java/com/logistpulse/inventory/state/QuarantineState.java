package com.logistpulse.inventory.state;

import com.logistpulse.inventory.model.BatchContext;

public class QuarantineState implements BatchState {
    @Override
    public void store(BatchContext context) {
        System.out.println("[STATE] Lote " + context.getBatchId() + ": Cuarentena -> Almacenado (liberado)");
        context.setState(new StoredState());
    }

    @Override
    public void reserve(BatchContext context) {
        throw new IllegalStateException("Transición inválida: Un lote en CUARENTENA no puede reservarse.");
    }

    @Override
    public void ship(BatchContext context) {
        throw new IllegalStateException("Transición inválida: Un lote en CUARENTENA no puede enviarse.");
    }

    @Override
    public void quarantine(BatchContext context) {
        throw new IllegalStateException("Transición inválida: El lote ya está en CUARENTENA.");
    }

    @Override
    public String getStateName() {
        return "CUARENTENA";
    }
}
