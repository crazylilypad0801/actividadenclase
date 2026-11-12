package com.logistpulse.inventory.strategy.replenishment;

public interface ReplenishmentPolicy {
    int calculateOrderQuantity(int currentStock, int dailyDemand);
    String getPolicyName();
}
