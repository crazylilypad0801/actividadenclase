package com.logistpulse.inventory.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class InventoryReceiver {
    private final ConcurrentHashMap<String, Integer> stockBySku = new ConcurrentHashMap<>();

    public int receive(String sku, int quantity) {
        return stockBySku.merge(sku, quantity, Math::addExact);
    }

    public int reconcile(String sku, int countedQuantity) {
        stockBySku.put(sku, countedQuantity);
        return countedQuantity;
    }
}
