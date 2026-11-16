package com.logistpulse.inventory.command;

import com.logistpulse.inventory.service.InventoryReceiver;
import com.logistpulse.inventory.service.LedgerService;

public class ReleaseCommand extends AbstractInventoryCommand {
    public ReleaseCommand(
            InventoryReceiver receiver, LedgerService ledger, String commandId,
            String sku, int quantity, String location) {
        super(receiver, ledger, commandId, sku, quantity, location, null);
    }

    @Override
    protected CommandOperation operation() {
        return CommandOperation.RELEASE;
    }

    @Override
    protected void executeOperation() {
        receiver.release(sku, quantity, location);
    }

    @Override
    protected void undoOperation() {
        receiver.reserve(sku, quantity, location);
    }
}
