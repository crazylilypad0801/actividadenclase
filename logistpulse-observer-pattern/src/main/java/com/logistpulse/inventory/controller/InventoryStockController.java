package com.logistpulse.inventory.controller;

import com.logistpulse.inventory.event.StockChangedEvent;
import com.logistpulse.inventory.service.InventoryStockService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inventory")
public class InventoryStockController {
    private final InventoryStockService inventoryStockService;

    public InventoryStockController(InventoryStockService inventoryStockService) {
        this.inventoryStockService = inventoryStockService;
    }

    @PostMapping("/{sku}/adjustments")
    public StockChangedEvent adjustStock(@PathVariable String sku, @RequestParam int delta) {
        return inventoryStockService.adjustStock(sku, delta);
    }
}
