package com.logistpulse.inventory.dto;

public class ReservationRequest {
    private String sku;
    private int quantity;
    private boolean quarantinedOrExpired;

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public boolean isQuarantinedOrExpired() { return quarantinedOrExpired; }
    public void setQuarantinedOrExpired(boolean quarantinedOrExpired) { this.quarantinedOrExpired = quarantinedOrExpired; }
}
