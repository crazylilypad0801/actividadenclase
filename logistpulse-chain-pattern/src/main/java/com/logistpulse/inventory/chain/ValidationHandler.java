package com.logistpulse.inventory.chain;

import com.logistpulse.inventory.dto.ReservationRequest;

public abstract class ValidationHandler {
    protected ValidationHandler next;

    public ValidationHandler linkWith(ValidationHandler next) {
        this.next = next;
        return next;
    }

    public abstract void validate(ReservationRequest request);

    protected void checkNext(ReservationRequest request) {
        if (next != null) {
            next.validate(request);
        }
    }
}
