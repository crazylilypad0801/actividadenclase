package com.logistpulse.inventory.service;

import com.logistpulse.inventory.command.AdjustCommand;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CommandInvokerTest {
    @Test
    void successfulCommandCanBeUndoneAndIsAudited() {
        InventoryReceiver receiver = new InventoryReceiver();
        LedgerService ledger = new LedgerService();
        CommandInvoker invoker = new CommandInvoker(ledger);
        AdjustCommand command = new AdjustCommand(receiver, ledger, "cmd-1", "SKU-1", 20, "MAIN");

        assertTrue(invoker.submit(command));
        assertFalse(invoker.submit(
                new AdjustCommand(receiver, ledger, "cmd-1", "SKU-1", 20, "MAIN")));
        assertEquals(20, receiver.getStock("SKU-1", "MAIN"));

        assertEquals("cmd-1", invoker.undoLast());
        assertEquals(0, receiver.getStock("SKU-1", "MAIN"));
        assertEquals("UNDO", ledger.getEntries().get(1).action());
    }
}
