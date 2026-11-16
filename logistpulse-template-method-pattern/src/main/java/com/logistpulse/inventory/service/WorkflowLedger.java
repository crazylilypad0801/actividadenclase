package com.logistpulse.inventory.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class WorkflowLedger {
    private final List<WorkflowRecord> records = new CopyOnWriteArrayList<>();

    public void record(WorkflowRecord record) {
        records.add(record);
    }

    public List<WorkflowRecord> getRecords() {
        return List.copyOf(records);
    }
}
