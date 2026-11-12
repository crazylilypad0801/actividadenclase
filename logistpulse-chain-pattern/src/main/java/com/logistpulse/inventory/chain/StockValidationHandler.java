package com.logistpulse.inventory.chain;

import com.logistpulse.inventory.dto.ReservationRequest;

public class StockValidationHandler extends ValidationHandler {
    @Override
    public void validate(ReservationRequest request) {
        if (request.getQuantity() <= 0 || request.getQuantity() > 500) {
            throw new IllegalArgumentException("Rechazado en Stock: Cantidad solicitada no disponible.");
        }
        System.out.println("[CHAIN] 2. Stock disponible validado: " + request.getQuantity() + " unidades.");
        checkNext(request);
    }
}
