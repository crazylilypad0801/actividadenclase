package com.logistpulse.inventory.state;

import com.logistpulse.inventory.model.BatchContext;

public class ReceivedState implements BatchState {
    @Override
    public void store(BatchContext context) {
        System.out.println("[STATE] Lote " + context.getBatchId() + ": Recibido -> Almacenado");
        context.setState(new StoredState());
    }

    @Override
    public void reserve(BatchContext context) {
        throw new IllegalStateException("Transición inválida: No se puede reservar un lote en estado RECIBIDO.");
    }

    @Override
    public void ship(BatchContext context) {
        throw new IllegalStateException("Transición inválida: No se puede enviar un lote en estado RECIBIDO.");
    }

    @Override
    public void quarantine(BatchContext context) {
        throw new IllegalStateException("Transición inválida: Debe almacenarse antes de pasar a cuarentena.");
    }

    @Override
    public String getStateName() {
        return "RECIBIDO";
    }
}
