package com.logistpulse.inventory.service;

import com.logistpulse.inventory.event.StockChangedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class StockLedgerListener {
    private final List<StockChangedEvent> entries = new CopyOnWriteArrayList<>();

    @EventListener
    public void onStockChanged(StockChangedEvent event) {
        entries.add(event);
    }

    public List<StockChangedEvent> getEntries() {
        return List.copyOf(entries);
    }
}
