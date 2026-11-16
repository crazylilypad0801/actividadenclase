package com.logistpulse.inventory.service;

import org.springframework.stereotype.Service;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LedgerService {
    private final Set<String> processedCommands = ConcurrentHashMap.newKeySet();

    public boolean exists(String commandId) {
        return processedCommands.contains(commandId);
    }

    public void record(String commandId, String action, String sku, int quantity) {
        processedCommands.add(commandId);
        System.out.println("[LEDGER AUDIT] Comando registrado -> ID: " + commandId + " | Acción: " + action + " | SKU: " + sku + " | Cantidad: " + quantity);
    }
}
