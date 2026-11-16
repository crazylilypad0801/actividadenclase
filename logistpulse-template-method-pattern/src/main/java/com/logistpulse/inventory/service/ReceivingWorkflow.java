package com.logistpulse.inventory.service;

import org.springframework.stereotype.Service;

@Service
public class ReceivingWorkflow extends InventoryWorkflow {
    public ReceivingWorkflow(InventoryReceiver receiver, WorkflowLedger ledger, WorkflowNotifier notifier) {
        super(receiver, ledger, notifier);
    }

    @Override
    protected void validateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Received quantity must be positive.");
        }
    }

    @Override
    protected int execute(InventoryReceiver receiver, String sku, int quantity) {
        return receiver.receive(sku, quantity);
    }

    @Override
    protected String getWorkflowName() {
        return "RECEIVING";
    }
}
