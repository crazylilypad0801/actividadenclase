package com.logistpulse.inventory.service;

import org.springframework.stereotype.Service;

@Service
public class CycleCountWorkflow extends InventoryWorkflow {
    public CycleCountWorkflow(InventoryReceiver receiver, WorkflowLedger ledger, WorkflowNotifier notifier) {
        super(receiver, ledger, notifier);
    }

    @Override
    protected void validateQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Counted quantity must not be negative.");
        }
    }

    @Override
    protected int execute(InventoryReceiver receiver, String sku, int quantity) {
        return receiver.reconcile(sku, quantity);
    }

    @Override
    protected String getWorkflowName() {
        return "CYCLE_COUNT";
    }
}
