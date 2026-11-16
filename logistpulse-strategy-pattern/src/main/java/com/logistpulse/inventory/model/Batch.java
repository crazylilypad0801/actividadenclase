package com.logistpulse.inventory.model;

import java.time.LocalDate;
import java.util.Objects;

public class Batch {
    private final String batchId;
    private final LocalDate receivedAt;
    private final LocalDate expirationDate;

    public Batch(String batchId, LocalDate receivedAt, LocalDate expirationDate) {
        this.batchId = Objects.requireNonNull(batchId, "batchId must not be null");
        this.receivedAt = Objects.requireNonNull(receivedAt, "receivedAt must not be null");
        this.expirationDate = Objects.requireNonNull(expirationDate, "expirationDate must not be null");
    }

    public String getBatchId() { return batchId; }
    public LocalDate getReceivedAt() { return receivedAt; }
    public LocalDate getExpirationDate() { return expirationDate; }
}
