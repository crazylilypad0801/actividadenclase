package com.logistpulse.inventory.command;

import com.logistpulse.inventory.service.InventoryReceiver;
import com.logistpulse.inventory.service.LedgerService;

public class TransferCommand extends AbstractInventoryCommand {
    public TransferCommand(
            InventoryReceiver receiver, LedgerService ledger, String commandId,
            String sku, int quantity, String sourceLocation, String destinationLocation) {
        super(receiver, ledger, commandId, sku, quantity, sourceLocation, destinationLocation);
    }

    @Override
    protected CommandOperation operation() {
        return CommandOperation.TRANSFER;
    }

    @Override
    protected void executeOperation() {
        receiver.transfer(sku, quantity, location, destinationLocation);
    }

    @Override
    protected void undoOperation() {
        receiver.transfer(sku, quantity, destinationLocation, location);
    }
}
