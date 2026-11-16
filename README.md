# Patrones de LOGISTPULSE Smart Inventory

Este espacio de trabajo contiene ejemplos pequeños e independientes, implementados con Spring Boot, de los seis patrones de comportamiento de la presentación. Cada proyecto es un prototipo didáctico en memoria, no un servicio de inventario listo para producción.

| Patrón | Proyecto | Ejemplo |
| --- | --- | --- |
| Comando | [logistpulse-command-pattern](./logistpulse-command-pattern) | Comandos para reservar, liberar, ajustar y transferir; idempotencia basada en los datos; deshacer en orden LIFO y un ledger estructurado. |
| Cadena de responsabilidad | [logistpulse-chain-pattern](./logistpulse-chain-pattern) | Las validaciones de una reserva se ejecutan en orden: SKU, stock, lote y capacidad. |
| Estrategia | [logistpulse-strategy-pattern](./logistpulse-strategy-pattern) | Picking FEFO/FIFO/LIFO y reposición EOQ/MIN_MAX/ROP. |
| Estado | [logistpulse-state-pattern](./logistpulse-state-pattern) | Un lote delega las transiciones de su ciclo de vida al estado actual. |
| Observador | [logistpulse-observer-pattern](./logistpulse-observer-pattern) | Los cambios de stock se publican a los listeners de reposición, PULSEFLEET y auditoría. |
| Método plantilla | [logistpulse-template-method-pattern](./logistpulse-template-method-pattern) | Recepción y conteo cíclico comparten el flujo validar/ejecutar/registrar/notificar. |

## Comando

Cada operación es un objeto comando. El invocador conserva los comandos ejecutados para deshacerlos en orden LIFO, y el ledger compara todos los datos de la operación asociados a una clave de idempotencia.

```mermaid
classDiagram
    class ReservationController {
        +createReservation(commandId, request)
        +undoLast()
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
        #executeOperation()
        #undoOperation()
    }
    class ReserveCommand
    class ReleaseCommand
    class AdjustCommand
    class TransferCommand
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
    class CommandInvoker {
        +submit(command) boolean
        +undoLast() String
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
    ReservationController --> CommandInvoker : envía una reserva
    InventoryOperationController --> InventoryCommandFactory : crea
    InventoryOperationController --> CommandInvoker : envía
    InventoryCommandFactory ..> OperationRequest : lee
    InventoryCommandFactory ..> InventoryCommand : crea
    CommandInvoker ..> InventoryCommand : ejecuta y deshace
    AbstractInventoryCommand --> InventoryReceiver : ejecuta y compensa
    AbstractInventoryCommand --> LedgerService : verifica y registra
```

## Cadena de responsabilidad

El servicio de validación enlaza los manejadores en orden. Cada manejador rechaza una entrada inválida o la pasa al siguiente eslabón.

```mermaid
classDiagram
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
    class ReservationRequest {
        -sku String
        -quantity int
        -quarantinedOrExpired boolean
    }

    ValidationHandler <|-- SkuValidationHandler
    ValidationHandler <|-- StockValidationHandler
    ValidationHandler <|-- BatchValidationHandler
    ValidationHandler <|-- CapacityValidationHandler
    ReservationValidationController --> ReservationValidationService : valida
    ReservationValidationService --> SkuValidationHandler : inicia la cadena
    SkuValidationHandler --> StockValidationHandler : siguiente
    StockValidationHandler --> BatchValidationHandler : siguiente
    BatchValidationHandler --> CapacityValidationHandler : siguiente
    ValidationHandler ..> ReservationRequest : verifica
```

## Estrategia

El contexto elige una política de picking o reposición mediante una clave y delega el algoritmo a dicha política.

```mermaid
classDiagram
    class StrategyController {
        +pickBatch(policy)
        +calculateReplenishment(policy, currentStock, dailyDemand)
    }
    class InventoryStrategyContext {
        -pickingPolicies Map
        -replenishmentPolicies Map
        +executePicking(policyKey, batches) Batch
        +executeReplenishment(policyKey, currentStock, dailyDemand) int
        +getSampleBatches() List
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

    StrategyController --> InventoryStrategyContext : delega
    InventoryStrategyContext ..> PickingPolicy : selecciona
    InventoryStrategyContext ..> ReplenishmentPolicy : selecciona
    InventoryStrategyContext ..> Batch : proporciona
    PickingPolicy <|.. FifoPickingPolicy
    PickingPolicy <|.. LifoPickingPolicy
    PickingPolicy <|.. FefoPickingPolicy
    ReplenishmentPolicy <|.. EoqReplenishmentPolicy
    ReplenishmentPolicy <|.. MinMaxReplenishmentPolicy
    ReplenishmentPolicy <|.. RopReplenishmentPolicy
    PickingPolicy ..> Batch : selecciona
```

## Estado

`BatchContext` delega cada acción del ciclo de vida al estado actual. Si la transición tiene éxito, el método del estado devuelve el siguiente estado; si la rechaza, lanza una excepción y el estado actual se conserva.

```mermaid
classDiagram
    class BatchStateController {
        +transition(batchId, action)
    }
    class BatchLifecycleService {
        -batches Map
        +getOrCreateBatch(batchId) BatchContext
        +applyTransition(batchId, action) String
    }
    class BatchContext {
        -batchId String
        -currentState BatchState
        +store()
        +reserve()
        +ship()
        +quarantine()
        +getCurrentStateName() String
    }
    class BatchState {
        <<interface>>
        +store(context) BatchState
        +reserve(context) BatchState
        +ship(context) BatchState
        +quarantine(context) BatchState
        +getStateName() String
    }
    class ReceivedState
    class StoredState
    class ReservedState
    class ShippedState
    class QuarantineState

    BatchStateController --> BatchLifecycleService : delega
    BatchLifecycleService o-- BatchContext : conserva
    BatchContext --> BatchState : delega al estado actual
    BatchState <|.. ReceivedState
    BatchState <|.. StoredState
    BatchState <|.. ReservedState
    BatchState <|.. ShippedState
    BatchState <|.. QuarantineState
```

## Observador

El servicio de inventario publica un `StockChangedEvent`; suscriptores independientes gestionan alertas de reposición, notificaciones para PULSEFLEET y registros de auditoría.

```mermaid
classDiagram
    class InventoryStockController {
        +adjustStock(sku, delta) StockChangedEvent
    }
    class InventoryStockService {
        -stockBySku Map
        +adjustStock(sku, delta) StockChangedEvent
        +getStock(sku) int
    }
    class ApplicationEventPublisher {
        <<interface>>
        +publishEvent(event)
    }
    class StockChangedEvent {
        <<event>>
        +sku String
        +previousQuantity int
        +currentQuantity int
        +occurredAt Instant
    }
    class ReplenishmentListener {
        +onStockChanged(event)
    }
    class PulseFleetListener {
        +onStockChanged(event)
    }
    class StockLedgerListener {
        +onStockChanged(event)
    }

    InventoryStockController --> InventoryStockService : ajusta
    InventoryStockService ..> StockChangedEvent : crea
    InventoryStockService --> ApplicationEventPublisher : publica
    ApplicationEventPublisher ..> ReplenishmentListener : distribuye
    ApplicationEventPublisher ..> PulseFleetListener : distribuye
    ApplicationEventPublisher ..> StockLedgerListener : distribuye
    ReplenishmentListener ..> StockChangedEvent : observa
    PulseFleetListener ..> StockChangedEvent : observa
    StockLedgerListener ..> StockChangedEvent : observa
```

## Método plantilla

`InventoryWorkflow.process()` fija la secuencia —validar, ejecutar, registrar y notificar— mientras que la recepción y el conteo cíclico definen su propia validación y operación de inventario.

```mermaid
classDiagram
    class InventoryWorkflow {
        <<abstract>>
        +process(sku, quantity) WorkflowRecord
        #validateQuantity(quantity)*
        #execute(receiver, sku, quantity) int*
        #getWorkflowName() String*
    }
    class ReceivingWorkflow {
        #validateQuantity(quantity)
        #execute(receiver, sku, quantity) int
        #getWorkflowName() String
    }
    class CycleCountWorkflow {
        #validateQuantity(quantity)
        #execute(receiver, sku, quantity) int
        #getWorkflowName() String
    }
    class InventoryWorkflowController {
        +receive(request) WorkflowRecord
        +cycleCount(request) WorkflowRecord
    }
    class InventoryReceiver {
        +receive(sku, quantity) int
        +reconcile(sku, countedQuantity) int
    }
    class WorkflowLedger {
        +record(record)
    }
    class WorkflowNotifier {
        +notifyCompleted(record)
    }
    class WorkflowRecord {
        <<record>>
        +workflow String
        +sku String
        +quantity int
        +stockAfter int
    }

    InventoryWorkflow <|-- ReceivingWorkflow
    InventoryWorkflow <|-- CycleCountWorkflow
    InventoryWorkflowController --> ReceivingWorkflow : invoca
    InventoryWorkflowController --> CycleCountWorkflow : invoca
    InventoryWorkflow --> InventoryReceiver : ejecuta
    InventoryWorkflow --> WorkflowLedger : registra
    InventoryWorkflow --> WorkflowNotifier : notifica
    InventoryWorkflow ..> WorkflowRecord : crea
```

## Ejecutar un ejemplo

Ejecuta Maven desde el directorio de cada proyecto:

```text
mvn spring-boot:run
```

Ejecuta sus pruebas con:

```text
mvn test
```

Los ejemplos usan estado en memoria y no ofrecen persistencia, transacciones ni garantías de concurrencia para uso en producción.
