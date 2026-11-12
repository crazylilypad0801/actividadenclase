package com.logistpulse.inventory.strategy.replenishment;

import org.springframework.stereotype.Component;

@Component("EOQ")
public class EoqReplenishmentPolicy implements ReplenishmentPolicy {
    private static final double ORDER_COST = 50.0;
    private static final double HOLDING_COST = 2.0;

    @Override
    public int calculateOrderQuantity(int currentStock, int dailyDemand) {
        double annualDemand = dailyDemand * 365.0;
        return (int) Math.round(Math.sqrt((2 * annualDemand * ORDER_COST) / HOLDING_COST));
    }

    @Override
    public String getPolicyName() {
        return "EOQ";
    }
}
