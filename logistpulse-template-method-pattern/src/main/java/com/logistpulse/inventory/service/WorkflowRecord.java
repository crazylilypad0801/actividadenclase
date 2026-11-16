package com.logistpulse.inventory.service;

public record WorkflowRecord(String workflow, String sku, int quantity, int stockAfter) {
}
