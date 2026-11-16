package com.logistpulse.inventory.service;

import com.logistpulse.inventory.event.StockChangedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class PulseFleetListener {
    private final List<StockChangedEvent> notifications = new CopyOnWriteArrayList<>();

    @EventListener
    public void onStockChanged(StockChangedEvent event) {
        notifications.add(event);
    }

    public List<StockChangedEvent> getNotifications() {
        return List.copyOf(notifications);
    }
}
