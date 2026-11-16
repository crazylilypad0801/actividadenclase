package com.logistpulse.inventory.state;

import com.logistpulse.inventory.model.BatchContext;

public class ShippedState implements BatchState {
    @Override
    public void store(BatchContext context) {
        throw new IllegalStateException("Transición inválida: El lote ya fue ENVIADO (estado final).");
    }

    @Override
    public void reserve(BatchContext context) {
        throw new IllegalStateException("Transición inválida: El lote ya fue ENVIADO (estado final).");
    }

    @Override
    public void ship(BatchContext context) {
        throw new IllegalStateException("Transición inválida: El lote ya fue ENVIADO.");
    }

    @Override
    public void quarantine(BatchContext context) {
        throw new IllegalStateException("Transición inválida: El lote ya fue ENVIADO.");
    }

    @Override
    public String getStateName() {
        return "ENVIADO";
    }
}
