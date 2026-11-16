package com.logistpulse.inventory.state;

import com.logistpulse.inventory.model.BatchContext;

public class StoredState implements BatchState {
    @Override
    public void store(BatchContext context) {
        throw new IllegalStateException("Transición inválida: El lote ya está ALMACENADO.");
    }

    @Override
    public void reserve(BatchContext context) {
        System.out.println("[STATE] Lote " + context.getBatchId() + ": Almacenado -> Reservado");
        context.setState(new ReservedState());
    }

    @Override
    public void ship(BatchContext context) {
        throw new IllegalStateException("Transición inválida: No se puede enviar un lote sin reservarlo primero.");
    }

    @Override
    public void quarantine(BatchContext context) {
        System.out.println("[STATE] Lote " + context.getBatchId() + ": Almacenado -> Cuarentena");
        context.setState(new QuarantineState());
    }

    @Override
    public String getStateName() {
        return "ALMACENADO";
    }
}
