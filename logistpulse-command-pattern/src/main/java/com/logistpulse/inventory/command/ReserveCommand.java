package com.logistpulse.inventory.command;

import com.logistpulse.inventory.service.InventoryReceiver;
import com.logistpulse.inventory.service.LedgerService;

public class ReserveCommand extends AbstractInventoryCommand {
    public ReserveCommand(
            InventoryReceiver receiver, LedgerService ledger, String commandId,
            String sku, int quantity, String location) {
        super(receiver, ledger, commandId, sku, quantity, location, null);
    }

    @Override
    protected CommandOperation operation() {
        return CommandOperation.RESERVE;
    }

    @Override
    protected void executeOperation() {
        receiver.reserve(sku, quantity, location);
    }

    @Override
    protected void undoOperation() {
        receiver.release(sku, quantity, location);
    }
}
