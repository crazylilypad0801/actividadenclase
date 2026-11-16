package com.logistpulse.inventory.service;

import java.time.Instant;

public record LedgerEntry(
        String commandId,
        String action,
        String sku,
        int quantity,
        String location,
        String destinationLocation,
        Instant occurredAt) {
}
