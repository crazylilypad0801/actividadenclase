package com.logistpulse.inventory.model;

import java.time.LocalDate;

public class Batch {
    private String batchId;
    private LocalDate receivedAt;
    private LocalDate expirationDate;

    public Batch(String batchId, LocalDate receivedAt, LocalDate expirationDate) {
        this.batchId = batchId;
        this.receivedAt = receivedAt;
        this.expirationDate = expirationDate;
    }

    public String getBatchId() { return batchId; }
    public LocalDate getReceivedAt() { return receivedAt; }
    public LocalDate getExpirationDate() { return expirationDate; }
}
