package com.logistpulse.inventory.service;

import com.logistpulse.inventory.chain.*;
import com.logistpulse.inventory.dto.ReservationRequest;
import org.springframework.stereotype.Service;

@Service
public class ReservationValidationService {
    private final ValidationHandler chain;

    public ReservationValidationService() {
        this.chain = new SkuValidationHandler();
        this.chain
            .linkWith(new StockValidationHandler())
            .linkWith(new BatchValidationHandler())
            .linkWith(new CapacityValidationHandler());
    }

    public void validateReservation(ReservationRequest request) {
        chain.validate(request);
    }
}
