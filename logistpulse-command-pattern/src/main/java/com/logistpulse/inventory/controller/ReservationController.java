package com.logistpulse.inventory.controller;

import com.logistpulse.inventory.command.InventoryCommand;
import com.logistpulse.inventory.command.ReserveCommand;
import com.logistpulse.inventory.dto.ReservationRequest;
import com.logistpulse.inventory.service.InventoryReceiver;
import com.logistpulse.inventory.service.IdempotencyConflictException;
import com.logistpulse.inventory.service.LedgerService;
import com.logistpulse.inventory.service.CommandInvoker;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final InventoryReceiver inventoryReceiver;
    private final LedgerService ledgerService;
    private final CommandInvoker commandInvoker;

    public ReservationController(
            InventoryReceiver inventoryReceiver, LedgerService ledgerService, CommandInvoker commandInvoker) {
        this.inventoryReceiver = inventoryReceiver;
        this.ledgerService = ledgerService;
        this.commandInvoker = commandInvoker;
    }

    @PostMapping
    public ResponseEntity<String> createReservation(
            @RequestHeader("X-Command-Id") String commandId,
            @RequestBody ReservationRequest request) {

        InventoryCommand command = new ReserveCommand(
            inventoryReceiver,
            ledgerService,
            commandId,
            request.getSku(),
            request.getQuantity(),
            "MAIN"
        );

        try {
            boolean executed = commandInvoker.submit(command);
            String message = executed
                    ? "Comando procesado correctamente bajo el ID: " + commandId
                    : "Comando ya procesado bajo el ID: " + commandId;
            return ResponseEntity.ok(message);
        } catch (IdempotencyConflictException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PostMapping("/undo")
    public ResponseEntity<String> undoLast() {
        try {
            return ResponseEntity.ok("Undid command: " + commandInvoker.undoLast());
        } catch (IllegalStateException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }
}
