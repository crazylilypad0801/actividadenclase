# LOGISTPULSE Smart Inventory patterns

This workspace contains small, independently runnable Spring Boot examples of the six behavioral patterns in the presentation. Each project is an in-memory teaching prototype, not a production inventory service.

| Pattern | Project | Example |
| --- | --- | --- |
| Command | [logistpulse-command-pattern](./logistpulse-command-pattern) | Reserve/release/adjust/transfer commands, payload-aware idempotency, LIFO undo, and a structured ledger. |
| Chain of Responsibility | [logistpulse-chain-pattern](./logistpulse-chain-pattern) | Reservation checks run in SKU, stock, batch, and capacity order. |
| Strategy | [logistpulse-strategy-pattern](./logistpulse-strategy-pattern) | FEFO/FIFO/LIFO picking and EOQ/MIN_MAX/ROP replenishment. |
| State | [logistpulse-state-pattern](./logistpulse-state-pattern) | A batch delegates lifecycle transitions to its current state. |
| Observer | [logistpulse-observer-pattern](./logistpulse-observer-pattern) | Stock changes are published to replenishment, PULSEFLEET, and ledger listeners. |
| Template Method | [logistpulse-template-method-pattern](./logistpulse-template-method-pattern) | Receiving and cycle count share a fixed validate/execute/record/notify workflow. |

## Running an example

Run Maven from an individual project directory:

```text
mvn spring-boot:run
```

Run its focused tests with:

```text
mvn test
```

The examples use in-memory state and do not provide persistence, transactions, or concurrency guarantees for production use.
