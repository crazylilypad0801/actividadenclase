package com.logistpulse.inventory.event;

import java.time.Instant;

public record StockChangedEvent(String sku, int previousQuantity, int currentQuantity, Instant occurredAt) {
}
