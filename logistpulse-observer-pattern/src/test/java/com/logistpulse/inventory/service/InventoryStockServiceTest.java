package com.logistpulse.inventory.service;

import com.logistpulse.inventory.event.StockChangedEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class InventoryStockServiceTest {
    @Autowired
    private InventoryStockService inventoryStockService;

    @Autowired
    private ReplenishmentListener replenishmentListener;

    @Autowired
    private PulseFleetListener pulseFleetListener;

    @Autowired
    private StockLedgerListener stockLedgerListener;

    @Test
    void publishesStockChangesToIndependentSubscribers() {
        String sku = "OBSERVER-LOW-STOCK";

        StockChangedEvent event = inventoryStockService.adjustStock(sku, 10);

        assertEquals(0, event.previousQuantity());
        assertEquals(10, event.currentQuantity());
        assertEquals(event, replenishmentListener.getReplenishmentAlerts().get(0));
        assertEquals(event, pulseFleetListener.getNotifications().get(0));
        assertEquals(event, stockLedgerListener.getEntries().get(0));
    }

    @Test
    void rejectsAdjustmentsThatWouldCreateNegativeStock() {
        assertThrows(IllegalArgumentException.class,
                () -> inventoryStockService.adjustStock("OBSERVER-NEGATIVE", -1));
        assertEquals(0, inventoryStockService.getStock("OBSERVER-NEGATIVE"));
    }
}
