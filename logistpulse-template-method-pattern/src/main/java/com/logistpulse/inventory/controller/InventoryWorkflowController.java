package com.logistpulse.inventory.controller;

import com.logistpulse.inventory.dto.InventoryWorkflowRequest;
import com.logistpulse.inventory.service.CycleCountWorkflow;
import com.logistpulse.inventory.service.ReceivingWorkflow;
import com.logistpulse.inventory.service.WorkflowRecord;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/workflows")
public class InventoryWorkflowController {
    private final ReceivingWorkflow receivingWorkflow;
    private final CycleCountWorkflow cycleCountWorkflow;

    public InventoryWorkflowController(
            ReceivingWorkflow receivingWorkflow, CycleCountWorkflow cycleCountWorkflow) {
        this.receivingWorkflow = receivingWorkflow;
        this.cycleCountWorkflow = cycleCountWorkflow;
    }

    @PostMapping("/receiving")
    public WorkflowRecord receive(@RequestBody InventoryWorkflowRequest request) {
        return receivingWorkflow.process(request.getSku(), request.getQuantity());
    }

    @PostMapping("/cycle-count")
    public WorkflowRecord cycleCount(@RequestBody InventoryWorkflowRequest request) {
        return cycleCountWorkflow.process(request.getSku(), request.getQuantity());
    }
}
