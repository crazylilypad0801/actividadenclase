package com.logistpulse.inventory.state;

import com.logistpulse.inventory.model.BatchContext;

public class ReservedState implements BatchState {
    @Override
    public BatchState store(BatchContext context) {
        throw new IllegalStateException("Transición inválida: El lote ya está RESERVADO.");
    }

    @Override
    public BatchState reserve(BatchContext context) {
        throw new IllegalStateException("Transición inválida: El lote ya está RESERVADO.");
    }

    @Override
    public BatchState ship(BatchContext context) {
        System.out.println("[STATE] Lote " + context.getBatchId() + ": Reservado -> Enviado");
        return new ShippedState();
    }

    @Override
    public BatchState quarantine(BatchContext context) {
        throw new IllegalStateException("Transición inválida: Un lote reservado no puede pasar directamente a cuarentena.");
    }

    @Override
    public String getStateName() {
        return "RESERVADO";
    }
}
