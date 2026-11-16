# LogistPulse Inventory Design Patterns

This repository contains three small Spring Boot examples of inventory workflows implemented with design patterns:

- **Command** — the root Maven project, which models inventory reservations as executable commands.
- **Chain of Responsibility** — `logistpulse-chain-pattern`, which validates a reservation through an ordered sequence of handlers.
- **Strategy** — `logistpulse-strategy-pattern`, which selects interchangeable batch-picking and replenishment policies.

## Command Pattern

`ReservationController` creates a `ReserveCommand` from the incoming reservation request and command ID. The command delegates the reservation to `InventoryReceiver` and records successful execution through `LedgerService`. The ledger check makes repeated command IDs idempotent.

```mermaid
classDiagram
    direction LR

    class ReservationController {
        +createReservation(commandId, request)
    }
    class InventoryCommand {
        <<interface>>
        +execute()
        +getCommandId() String
    }
    class ReserveCommand {
        -commandId String
        -sku String
        -quantity int
        +execute()
        +getCommandId() String
    }
    class InventoryReceiver {
        +reserve(sku, quantity)
    }
    class LedgerService {
        +exists(commandId) boolean
        +record(commandId, action, sku, quantity)
    }

    InventoryCommand <|.. ReserveCommand
    ReservationController ..> InventoryCommand : creates
    ReservationController ..> ReserveCommand : executes
    ReserveCommand --> InventoryReceiver : reserves inventory
    ReserveCommand --> LedgerService : checks and records
```

## Chain of Responsibility Pattern

The validation service builds this chain in order: SKU, stock, batch status, then capacity. Each handler rejects invalid requests or passes valid requests to the next handler.

```mermaid
classDiagram
    direction LR

    class ReservationValidationController {
        +validate(request)
    }
    class ReservationValidationService {
        -chain ValidationHandler
        +validateReservation(request)
    }
    class ValidationHandler {
        <<abstract>>
        #next ValidationHandler
        +linkWith(next) ValidationHandler
        +validate(request)*
        #checkNext(request)
    }
    class SkuValidationHandler
    class StockValidationHandler
    class BatchValidationHandler
    class CapacityValidationHandler

    ValidationHandler <|-- SkuValidationHandler
    ValidationHandler <|-- StockValidationHandler
    ValidationHandler <|-- BatchValidationHandler
    ValidationHandler <|-- CapacityValidationHandler
    ReservationValidationController ..> ReservationValidationService : calls
    ReservationValidationService --> SkuValidationHandler : starts chain
    SkuValidationHandler --> StockValidationHandler : next
    StockValidationHandler --> BatchValidationHandler : next
    BatchValidationHandler --> CapacityValidationHandler : next
```

## Strategy Pattern

`InventoryStrategyContext` selects a policy by key and delegates the work. Picking and replenishment are separate strategy families, each with its own interface and implementations. Unknown keys use FIFO for picking and MIN_MAX for replenishment.

```mermaid
classDiagram
    direction TB

    class StrategyController {
        +pickBatch(policy)
        +calculateReplenishment(policy, currentStock, dailyDemand)
    }
    class InventoryStrategyContext {
        -pickingPolicies Map
        -replenishmentPolicies Map
        +executePicking(policyKey, batches) Batch
        +executeReplenishment(policyKey, currentStock, dailyDemand) int
        +getSampleBatches() List~Batch~
    }
    class Batch {
        -batchId String
        -receivedAt LocalDate
        -expirationDate LocalDate
    }
    class PickingPolicy {
        <<interface>>
        +selectBatch(availableBatches) Batch
        +getPolicyName() String
    }
    class FifoPickingPolicy
    class LifoPickingPolicy
    class FefoPickingPolicy
    class ReplenishmentPolicy {
        <<interface>>
        +calculateOrderQuantity(currentStock, dailyDemand) int
        +getPolicyName() String
    }
    class EoqReplenishmentPolicy
    class MinMaxReplenishmentPolicy
    class RopReplenishmentPolicy

    StrategyController --> InventoryStrategyContext : delegates
    InventoryStrategyContext ..> PickingPolicy : selects
    InventoryStrategyContext ..> ReplenishmentPolicy : selects
    InventoryStrategyContext ..> Batch : supplies batches
    PickingPolicy <|.. FifoPickingPolicy
    PickingPolicy <|.. LifoPickingPolicy
    PickingPolicy <|.. FefoPickingPolicy
    ReplenishmentPolicy <|.. EoqReplenishmentPolicy
    ReplenishmentPolicy <|.. MinMaxReplenishmentPolicy
    ReplenishmentPolicy <|.. RopReplenishmentPolicy
    PickingPolicy ..> Batch : selects
```
