package com.logistpulse.inventory.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BatchContextTest {
    @Test
    void invalidTransitionLeavesCurrentStateUnchanged() {
        BatchContext batch = new BatchContext("B-1");

        assertThrows(IllegalStateException.class, batch::reserve);
        assertEquals("RECIBIDO", batch.getCurrentStateName());

        batch.store();
        batch.reserve();
        assertEquals("RESERVADO", batch.getCurrentStateName());
    }
}
