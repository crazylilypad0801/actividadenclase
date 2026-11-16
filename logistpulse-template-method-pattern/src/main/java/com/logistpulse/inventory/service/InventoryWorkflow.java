package com.logistpulse.inventory.service;

public abstract class InventoryWorkflow {
    private final InventoryReceiver receiver;
    private final WorkflowLedger ledger;
    private final WorkflowNotifier notifier;

    protected InventoryWorkflow(
            InventoryReceiver receiver, WorkflowLedger ledger, WorkflowNotifier notifier) {
        this.receiver = receiver;
        this.ledger = ledger;
        this.notifier = notifier;
    }

    public final WorkflowRecord process(String sku, int quantity) {
        validateCommon(sku);
        validateQuantity(quantity);
        int stockAfter = execute(receiver, sku, quantity);
        WorkflowRecord record = new WorkflowRecord(getWorkflowName(), sku, quantity, stockAfter);
        ledger.record(record);
        notifier.notifyCompleted(record);
        return record;
    }

    protected abstract void validateQuantity(int quantity);

    protected abstract int execute(InventoryReceiver receiver, String sku, int quantity);

    protected abstract String getWorkflowName();

    private void validateCommon(String sku) {
        if (sku == null || sku.isBlank()) {
            throw new IllegalArgumentException("SKU must not be blank.");
        }
    }
}
