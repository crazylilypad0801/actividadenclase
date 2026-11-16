package com.logistpulse.inventory.service;

import com.logistpulse.inventory.event.StockChangedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class InventoryStockService {
    private final ConcurrentHashMap<String, Integer> stockBySku = new ConcurrentHashMap<>();
    private final ApplicationEventPublisher eventPublisher;

    public InventoryStockService(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public StockChangedEvent adjustStock(String sku, int delta) {
        if (sku == null || sku.isBlank()) {
            throw new IllegalArgumentException("SKU must not be blank.");
        }
        if (delta == 0) {
            throw new IllegalArgumentException("Stock adjustment must not be zero.");
        }

        AtomicInteger previousQuantity = new AtomicInteger();
        int currentQuantity = stockBySku.compute(sku, (key, current) -> {
            int previous = current == null ? 0 : current;
            int updated = Math.addExact(previous, delta);
            if (updated < 0) {
                throw new IllegalArgumentException("Stock cannot become negative for SKU " + sku);
            }
            previousQuantity.set(previous);
            return updated;
        });

        StockChangedEvent event = new StockChangedEvent(
                sku, previousQuantity.get(), currentQuantity, Instant.now());
        eventPublisher.publishEvent(event);
        return event;
    }

    public int getStock(String sku) {
        return stockBySku.getOrDefault(sku, 0);
    }
}
