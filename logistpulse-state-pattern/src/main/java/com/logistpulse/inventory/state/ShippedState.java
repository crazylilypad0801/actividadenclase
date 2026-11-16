package com.logistpulse.inventory.state;

import com.logistpulse.inventory.model.BatchContext;

public class ShippedState implements BatchState {
    @Override
    public BatchState store(BatchContext context) {
        throw new IllegalStateException("Transición inválida: El lote ya fue ENVIADO (estado final).");
    }

    @Override
    public BatchState reserve(BatchContext context) {
        throw new IllegalStateException("Transición inválida: El lote ya fue ENVIADO (estado final).");
    }

    @Override
    public BatchState ship(BatchContext context) {
        throw new IllegalStateException("Transición inválida: El lote ya fue ENVIADO.");
    }

    @Override
    public BatchState quarantine(BatchContext context) {
        throw new IllegalStateException("Transición inválida: El lote ya fue ENVIADO.");
    }

    @Override
    public String getStateName() {
        return "ENVIADO";
    }
}
