package com.logistpulse.inventory.service;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class LedgerService {
    private final Map<String, CommandRecord> processedCommands = new HashMap<>();
    private final List<LedgerEntry> entries = new CopyOnWriteArrayList<>();

    public synchronized boolean executeOnce(
            String commandId, String action, String sku, int quantity,
            String location, String destinationLocation, Runnable operation) {
        if (commandId == null || commandId.isBlank()) {
            throw new IllegalArgumentException("Command ID must not be blank.");
        }
        if (operation == null) {
            throw new IllegalArgumentException("Command operation must not be null.");
        }

        CommandRecord requested = new CommandRecord(action, sku, quantity, location, destinationLocation);
        CommandRecord existing = processedCommands.get(commandId);
        if (existing != null) {
            if (!existing.equals(requested)) {
                throw new IdempotencyConflictException(
                        "Command ID was reused with different operation data: " + commandId);
            }
            return false;
        }

        operation.run();
        processedCommands.put(commandId, requested);
        entries.add(new LedgerEntry(
                commandId, action, sku, quantity, location, destinationLocation, Instant.now()));
        System.out.println("[LEDGER AUDIT] Command recorded: " + commandId + " | " + requested);
        return true;
    }

    public void recordUndo(String commandId) {
        entries.add(new LedgerEntry(commandId, "UNDO", null, 0, null, null, Instant.now()));
    }

    public List<LedgerEntry> getEntries() {
        return List.copyOf(entries);
    }

    private record CommandRecord(
            String action, String sku, int quantity, String location, String destinationLocation) { }
}
