package com.logistpulse.inventory.service;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LedgerServiceTest {
    @Test
    void executesMatchingCommandIdOnlyOnceAndRejectsDifferentPayload() {
        LedgerService ledger = new LedgerService();
        AtomicInteger executions = new AtomicInteger();

        assertTrue(ledger.executeOnce("cmd-1", "RESERVE", "SKU-1", 5, "MAIN", null, executions::incrementAndGet));
        assertFalse(ledger.executeOnce("cmd-1", "RESERVE", "SKU-1", 5, "MAIN", null, executions::incrementAndGet));
        assertThrows(IdempotencyConflictException.class,
                () -> ledger.executeOnce(
                        "cmd-1", "RESERVE", "SKU-1", 8, "MAIN", null, executions::incrementAndGet));

        assertEquals(1, executions.get());
    }

    @Test
    void failedOperationDoesNotConsumeCommandId() {
        LedgerService ledger = new LedgerService();

        assertThrows(IllegalStateException.class,
                () -> ledger.executeOnce("cmd-2", "RESERVE", "SKU-2", 1, "MAIN", null, () -> {
                    throw new IllegalStateException("temporary failure");
                }));
        assertTrue(ledger.executeOnce("cmd-2", "RESERVE", "SKU-2", 1, "MAIN", null, () -> { }));
    }
}
