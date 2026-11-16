package com.logistpulse.inventory.service;

import com.logistpulse.inventory.event.StockChangedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class ReplenishmentListener {
    private static final int REORDER_POINT = 20;
    private final List<StockChangedEvent> replenishmentAlerts = new CopyOnWriteArrayList<>();

    @EventListener
    public void onStockChanged(StockChangedEvent event) {
        if (event.currentQuantity() <= REORDER_POINT) {
            replenishmentAlerts.add(event);
        }
    }

    public List<StockChangedEvent> getReplenishmentAlerts() {
        return List.copyOf(replenishmentAlerts);
    }
}
