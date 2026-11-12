package com.logistpulse.inventory.chain;

import com.logistpulse.inventory.dto.ReservationRequest;

public class BatchValidationHandler extends ValidationHandler {
    @Override
    public void validate(ReservationRequest request) {
        if (request.isQuarantinedOrExpired()) {
            throw new IllegalArgumentException("Rechazado en Lote: El lote está vencido o en cuarentena.");
        }
        System.out.println("[CHAIN] 3. Estado del lote validado (apto para despacho).");
        checkNext(request);
    }
}
