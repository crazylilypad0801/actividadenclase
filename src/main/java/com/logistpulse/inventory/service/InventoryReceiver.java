package com.logistpulse.inventory.service;

import org.springframework.stereotype.Service;

@Service
public class InventoryReceiver {
    public void reserve(String sku, int quantity) {
        System.out.println("[INVENTORY] Stock reservado exitosamente: " + quantity + " unidades del SKU " + sku);
    }
}
