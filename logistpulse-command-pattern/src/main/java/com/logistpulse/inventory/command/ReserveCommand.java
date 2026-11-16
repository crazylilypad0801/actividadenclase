package com.logistpulse.inventory.command;

import com.logistpulse.inventory.service.InventoryReceiver;
import com.logistpulse.inventory.service.LedgerService;

public class ReserveCommand implements InventoryCommand {
    private final InventoryReceiver receiver;
    private final LedgerService ledger;
    private final String commandId;
    private final String sku;
    private final int quantity;

    public ReserveCommand(InventoryReceiver receiver, LedgerService ledger, String commandId, String sku, int quantity) {
        this.receiver = receiver;
        this.ledger = ledger;
        this.commandId = commandId;
        this.sku = sku;
        this.quantity = quantity;
    }

    @Override
    public void execute() {
        if (ledger.exists(commandId)) {
            System.out.println("[IDEMPOTENCY] El comando " + commandId + " ya fue ejecutado previamente.");
            return;
        }

        receiver.reserve(sku, quantity);
        ledger.record(commandId, "RESERVE", sku, quantity);
    }

    @Override
    public String getCommandId() {
        return commandId;
    }
}
