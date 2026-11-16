package com.logistpulse.inventory.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class InventoryReceiver {
    private final Map<StockKey, Integer> stockByLocation = new HashMap<>();

    public synchronized void reserve(String sku, int quantity, String location) {
        requirePositiveQuantity(quantity);
        changeStock(sku, location, -quantity);
    }

    public synchronized void release(String sku, int quantity, String location) {
        requirePositiveQuantity(quantity);
        changeStock(sku, location, quantity);
    }

    public synchronized void adjust(String sku, int delta, String location) {
        if (delta == 0) {
            throw new IllegalArgumentException("Adjustment must not be zero.");
        }
        changeStock(sku, location, delta);
    }

    public synchronized void transfer(
            String sku, int quantity, String sourceLocation, String destinationLocation) {
        requirePositiveQuantity(quantity);
        requireLocation(sourceLocation);
        requireLocation(destinationLocation);
        if (sourceLocation.equals(destinationLocation)) {
            throw new IllegalArgumentException("Source and destination locations must be different.");
        }

        StockKey source = new StockKey(requireSku(sku), sourceLocation);
        StockKey destination = new StockKey(sku, destinationLocation);
        int sourceStock = stockByLocation.getOrDefault(source, 0);
        if (sourceStock < quantity) {
            throw new IllegalArgumentException("Insufficient stock at location " + sourceLocation);
        }
        int destinationStock = Math.addExact(stockByLocation.getOrDefault(destination, 0), quantity);
        stockByLocation.put(source, sourceStock - quantity);
        stockByLocation.put(destination, destinationStock);
    }

    public synchronized int getStock(String sku, String location) {
        return stockByLocation.getOrDefault(new StockKey(requireSku(sku), requireLocation(location)), 0);
    }

    private void changeStock(String sku, String location, int delta) {
        StockKey key = new StockKey(requireSku(sku), requireLocation(location));
        int updated = Math.addExact(stockByLocation.getOrDefault(key, 0), delta);
        if (updated < 0) {
            throw new IllegalArgumentException("Stock cannot become negative at location " + location);
        }
        stockByLocation.put(key, updated);
    }

    private String requireSku(String sku) {
        if (sku == null || sku.isBlank()) {
            throw new IllegalArgumentException("SKU must not be blank.");
        }
        return sku;
    }

    private String requireLocation(String location) {
        if (location == null || location.isBlank()) {
            throw new IllegalArgumentException("Location must not be blank.");
        }
        return location;
    }

    private void requirePositiveQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive.");
        }
    }

    private record StockKey(String sku, String location) { }
}
