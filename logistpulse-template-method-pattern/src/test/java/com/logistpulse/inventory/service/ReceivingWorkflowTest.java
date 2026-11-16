package com.logistpulse.inventory.service;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class ReceivingWorkflowTest {
    @Test
    void runsSharedWorkflowStepsInOrder() {
        InventoryReceiver receiver = mock(InventoryReceiver.class);
        WorkflowLedger ledger = mock(WorkflowLedger.class);
        WorkflowNotifier notifier = mock(WorkflowNotifier.class);
        when(receiver.receive("SKU-1", 5)).thenReturn(15);
        ReceivingWorkflow workflow = new ReceivingWorkflow(receiver, ledger, notifier);

        WorkflowRecord result = workflow.process("SKU-1", 5);

        assertEquals(15, result.stockAfter());
        InOrder order = inOrder(receiver, ledger, notifier);
        order.verify(receiver).receive("SKU-1", 5);
        order.verify(ledger).record(result);
        order.verify(notifier).notifyCompleted(result);
    }

    @Test
    void validationFailureDoesNotExecuteOrRecord() {
        InventoryReceiver receiver = mock(InventoryReceiver.class);
        WorkflowLedger ledger = mock(WorkflowLedger.class);
        WorkflowNotifier notifier = mock(WorkflowNotifier.class);
        ReceivingWorkflow workflow = new ReceivingWorkflow(receiver, ledger, notifier);

        assertThrows(IllegalArgumentException.class, () -> workflow.process("SKU-1", 0));
        verifyNoInteractions(receiver, ledger, notifier);
    }
}
