package com.logistpulse.inventory.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class WorkflowNotifier {
    private final List<WorkflowRecord> notifications = new CopyOnWriteArrayList<>();

    public void notifyCompleted(WorkflowRecord record) {
        notifications.add(record);
    }

    public List<WorkflowRecord> getNotifications() {
        return List.copyOf(notifications);
    }
}
