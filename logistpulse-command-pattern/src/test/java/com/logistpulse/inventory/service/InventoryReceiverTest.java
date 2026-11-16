package com.logistpulse.inventory.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InventoryReceiverTest {
    @Test
    void transferMovesStockAndRejectsOverspendingWithoutMutation() {
        InventoryReceiver receiver = new InventoryReceiver();
        receiver.adjust("SKU-1", 50, "A");

        receiver.transfer("SKU-1", 20, "A", "B");

        assertEquals(30, receiver.getStock("SKU-1", "A"));
        assertEquals(20, receiver.getStock("SKU-1", "B"));
        assertThrows(IllegalArgumentException.class, () -> receiver.transfer("SKU-1", 31, "A", "B"));
        assertEquals(30, receiver.getStock("SKU-1", "A"));
        assertEquals(20, receiver.getStock("SKU-1", "B"));
    }
}
