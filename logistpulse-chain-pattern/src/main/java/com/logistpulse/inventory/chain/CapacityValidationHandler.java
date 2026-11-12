package com.logistpulse.inventory.chain;

import com.logistpulse.inventory.dto.ReservationRequest;

public class CapacityValidationHandler extends ValidationHandler {
    @Override
    public void validate(ReservationRequest request) {
        if (request.getQuantity() > 200) {
            throw new IllegalArgumentException("Rechazado en Capacidad: Excede la capacidad de preparación actual.");
        }
        System.out.println("[CHAIN] 4. Capacidad operativa validada.");
        checkNext(request);
    }
}
