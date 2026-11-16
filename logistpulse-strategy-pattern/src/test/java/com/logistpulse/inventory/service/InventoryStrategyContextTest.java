package com.logistpulse.inventory.service;

import com.logistpulse.inventory.model.Batch;
import com.logistpulse.inventory.strategy.picking.FifoPickingPolicy;
import com.logistpulse.inventory.strategy.replenishment.MinMaxReplenishmentPolicy;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InventoryStrategyContextTest {
    private final InventoryStrategyContext context = new InventoryStrategyContext(
            Map.of("FIFO", new FifoPickingPolicy()),
            Map.of("MIN_MAX", new MinMaxReplenishmentPolicy()));

    @Test
    void rejectsUnknownPolicyInsteadOfSilentlySelectingAnotherStrategy() {
        Batch batch = new Batch("B-1", LocalDate.now(), LocalDate.now().plusDays(1));

        assertThrows(IllegalArgumentException.class,
                () -> context.executePicking("FEFOO", List.of(batch)));
        assertThrows(IllegalArgumentException.class,
                () -> context.executeReplenishment("MINMAX", 10, 5));
    }

    @Test
    void rejectsInvalidInputsBeforeCallingStrategies() {
        assertThrows(IllegalArgumentException.class,
                () -> context.executePicking("FIFO", null));
        assertThrows(IllegalArgumentException.class,
                () -> context.executeReplenishment("MIN_MAX", -1, 5));
        assertThrows(IllegalArgumentException.class,
                () -> context.executeReplenishment("MIN_MAX", 10, -5));
    }

    @Test
    void acceptsCaseInsensitivePolicyNames() {
        Batch batch = new Batch("B-1", LocalDate.now(), LocalDate.now().plusDays(1));

        assertEquals(batch, context.executePicking("fifo", List.of(batch)));
    }
}
