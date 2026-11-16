# Command Pattern

This Spring Boot example models reserve, release, adjust, and transfer operations as command objects. `CommandInvoker` submits commands and undoes them in LIFO order; `InventoryReceiver` owns stock per SKU and location, and `LedgerService` records successful operations and undo events.

The ledger compares the complete operation payload for a reused ID: an identical retry is reported as a duplicate, while reusing the ID with different data returns a conflict. Failed operations do not consume their command IDs. Undo is retained in history if its compensating operation fails.

The state and ledger are in memory, so this is a pattern demonstration rather than production persistence or transaction management.

## Endpoints

- `POST /operations` with `X-Command-Id` and JSON fields `operation`, `sku`, `quantity`, and `location`; `destinationLocation` is required for transfers.
- `POST /reservations` remains a convenience endpoint for a reservation at location `MAIN`.
- `POST /reservations/undo` compensates the latest successful command.

Example operation body:

```json
{
  "operation": "ADJUST",
  "sku": "SKU-1",
  "quantity": 100,
  "location": "MAIN"
}
```

## Class diagram

```mermaid
classDiagram
    class ReservationController {
        +createReservation(commandId, request)
    }
    class InventoryOperationController {
        +execute(commandId, request)
    }
    class InventoryCommand {
        <<interface>>
        +execute() boolean
        +undo()
        +getCommandId() String
    }
    class AbstractInventoryCommand {
        <<abstract>>
        +execute() boolean
        +undo()
        #executeOperation()*
        #undoOperation()*
    }
    class ReleaseCommand
    class AdjustCommand
    class TransferCommand
    class ReserveCommand
    class CommandInvoker {
        +submit(command) boolean
        +undoLast() String
    }
    class InventoryCommandFactory {
        +create(commandId, request) InventoryCommand
    }
    class OperationRequest {
        +operation String
        +sku String
        +quantity int
        +location String
        +destinationLocation String
    }
    class InventoryReceiver {
        +reserve(sku, quantity, location)
        +release(sku, quantity, location)
        +adjust(sku, delta, location)
        +transfer(sku, quantity, source, destination)
    }
    class LedgerService {
        +executeOnce(commandId, operationData) boolean
        +recordUndo(commandId)
    }

    InventoryCommand <|.. AbstractInventoryCommand
    AbstractInventoryCommand <|-- ReserveCommand
    AbstractInventoryCommand <|-- ReleaseCommand
    AbstractInventoryCommand <|-- AdjustCommand
    AbstractInventoryCommand <|-- TransferCommand
    ReservationController --> CommandInvoker : submits
    InventoryOperationController --> InventoryCommandFactory : creates
    InventoryOperationController --> CommandInvoker : submits
    InventoryCommandFactory ..> OperationRequest : reads
    InventoryCommandFactory ..> InventoryCommand : creates
    CommandInvoker ..> InventoryCommand : invokes and undoes
    AbstractInventoryCommand --> InventoryReceiver : executes and compensates
    AbstractInventoryCommand --> LedgerService : checks and records
```
