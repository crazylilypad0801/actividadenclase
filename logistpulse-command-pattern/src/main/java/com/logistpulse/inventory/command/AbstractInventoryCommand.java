package com.logistpulse.inventory.command;

import com.logistpulse.inventory.service.InventoryReceiver;
import com.logistpulse.inventory.service.LedgerService;

public abstract class AbstractInventoryCommand implements InventoryCommand {
    protected final InventoryReceiver receiver;
    private final LedgerService ledger;
    private final String commandId;
    protected final String sku;
    protected final int quantity;
    protected final String location;
    protected final String destinationLocation;

    protected AbstractInventoryCommand(
            InventoryReceiver receiver, LedgerService ledger, String commandId,
            String sku, int quantity, String location, String destinationLocation) {
        this.receiver = receiver;
        this.ledger = ledger;
        this.commandId = commandId;
        this.sku = sku;
        this.quantity = quantity;
        this.location = location;
        this.destinationLocation = destinationLocation;
    }

    @Override
    public final boolean execute() {
        return ledger.executeOnce(commandId, operation().name(), sku, quantity,
                location, destinationLocation, this::executeOperation);
    }

    @Override
    public final void undo() {
        undoOperation();
    }

    @Override
    public final String getCommandId() {
        return commandId;
    }

    protected abstract CommandOperation operation();

    protected abstract void executeOperation();

    protected abstract void undoOperation();
}
