package com.logistpulse.inventory.command;

import com.logistpulse.inventory.service.InventoryReceiver;
import com.logistpulse.inventory.service.LedgerService;

public class AdjustCommand extends AbstractInventoryCommand {
    public AdjustCommand(
            InventoryReceiver receiver, LedgerService ledger, String commandId,
            String sku, int quantity, String location) {
        super(receiver, ledger, commandId, sku, quantity, location, null);
    }

    @Override
    protected CommandOperation operation() {
        return CommandOperation.ADJUST;
    }

    @Override
    protected void executeOperation() {
        receiver.adjust(sku, quantity, location);
    }

    @Override
    protected void undoOperation() {
        receiver.adjust(sku, Math.negateExact(quantity), location);
    }
}
