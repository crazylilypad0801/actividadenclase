package com.logistpulse.inventory.strategy.replenishment;

import org.springframework.stereotype.Component;

@Component("MIN_MAX")
public class MinMaxReplenishmentPolicy implements ReplenishmentPolicy {
    private static final int MIN_STOCK = 30;
    private static final int MAX_STOCK = 200;

    @Override
    public int calculateOrderQuantity(int currentStock, int dailyDemand) {
        return currentStock < MIN_STOCK ? (MAX_STOCK - currentStock) : 0;
    }

    @Override
    public String getPolicyName() {
        return "MIN_MAX";
    }
}
