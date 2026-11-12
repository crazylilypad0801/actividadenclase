package com.logistpulse.inventory.chain;

import com.logistpulse.inventory.dto.ReservationRequest;

public class SkuValidationHandler extends ValidationHandler {
    @Override
    public void validate(ReservationRequest request) {
        if (request.getSku() == null || request.getSku().isBlank()) {
            throw new IllegalArgumentException("Rechazado en SKU: El SKU no existe o es inválido.");
        }
        System.out.println("[CHAIN] 1. SKU validado: " + request.getSku());
        checkNext(request);
    }
}
