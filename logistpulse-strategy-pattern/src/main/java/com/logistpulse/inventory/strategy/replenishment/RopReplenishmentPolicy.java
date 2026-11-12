package com.logistpulse.inventory.strategy.replenishment;

import org.springframework.stereotype.Component;

@Component("ROP")
public class RopReplenishmentPolicy implements ReplenishmentPolicy {
    private static final int LEAD_TIME_DAYS = 3;
    private static final int SAFETY_STOCK = 20;

    @Override
    public int calculateOrderQuantity(int currentStock, int dailyDemand) {
        int reorderPoint = (dailyDemand * LEAD_TIME_DAYS) + SAFETY_STOCK;
        return currentStock <= reorderPoint ? (reorderPoint - currentStock + 50) : 0;
    }

    @Override
    public String getPolicyName() {
        return "ROP";
    }
}
