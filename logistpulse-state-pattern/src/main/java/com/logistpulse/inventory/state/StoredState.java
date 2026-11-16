package com.logistpulse.inventory.state;

import com.logistpulse.inventory.model.BatchContext;

public class StoredState implements BatchState {
    @Override
    public BatchState store(BatchContext context) {
        throw new IllegalStateException("Transición inválida: El lote ya está ALMACENADO.");
    }

    @Override
    public BatchState reserve(BatchContext context) {
        System.out.println("[STATE] Lote " + context.getBatchId() + ": Almacenado -> Reservado");
        return new ReservedState();
    }

    @Override
    public BatchState ship(BatchContext context) {
        throw new IllegalStateException("Transición inválida: No se puede enviar un lote sin reservarlo primero.");
    }

    @Override
    public BatchState quarantine(BatchContext context) {
        System.out.println("[STATE] Lote " + context.getBatchId() + ": Almacenado -> Cuarentena");
        return new QuarantineState();
    }

    @Override
    public String getStateName() {
        return "ALMACENADO";
    }
}
